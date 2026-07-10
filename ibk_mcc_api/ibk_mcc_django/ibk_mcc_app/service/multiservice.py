import grpc
from .proto import ibk_mcc_engine_control_pb2 as EC_proto
from .proto import ibk_mcc_engine_control_pb2_grpc as EC_grpc
from .proto import mcc_pb2 as MCC_proto
from .proto import mcc_pb2_grpc as MCC_grpc
from google.protobuf.empty_pb2 import Empty
import os, random, sys
from django.conf import settings
from django.db import connection
import mysql.connector
import logging
import time
from datetime import datetime

logger = logging.getLogger(__name__)
class MultiServiceClient:
    def __init__(self, endpoint):
        self.endpoint = endpoint

    def mcc_classify(self, parsed_text, float_text):
        try:
            # 만약 float_text가 리스트가 아니라면 리스트로 변환
            if isinstance(float_text, str):
                float_text = [float(float_text)]
            elif isinstance(float_text, list):
                # 리스트의 각 항목을 float로 변환
                float_text = [float(val) if isinstance(val, str) else val for val in float_text]
        except ValueError as e:
            print(f"Error converting float_text to float: {e}")
            raise Exception("Invalid float value in float_text.")

        # gRPC 요청을 위해 request 메시지 생성
        classify_request = MCC_proto.TextClassificationRequest(
            text=parsed_text,
            floats=float_text
        )

        # gRPC 서버와 연결
        
        with grpc.insecure_channel(self.endpoint) as channel:
            stub = EC_grpc.MccEngineControllerStub(channel)
            try:
                # gRPC 서버에 요청을 보내고 결과를 받아옵니다.
                result = stub.WrappedInference(classify_request)
                # print(result)
            except grpc.RpcError as e:
                # 오류 발생 시, 오류 메시지 출력
                print(f"gRPC error: {e}")
                raise Exception("Failed to call MCCClassify")

        # result의 'outputs'가 있는지 확인
        if hasattr(result, 'outputs'):
            # 'category_label_predictions' 필드를 추출합니다.
            category_label_predictions = result.outputs.category_label_predictions

            # RepeatedCompositeFieldContainer를 직렬화할 수 있는 형식으로 변환합니다.
            category_label_predictions_list = []
            for category in category_label_predictions:
                label_predictions_list = []
                for label_prediction in category.label_predictions:
                    label_predictions_list.append({
                        'label': label_prediction.label,
                        'probability': label_prediction.probability
                    })
                category_label_predictions_list.append({
                    'category': category.category,
                    'label_predictions': label_predictions_list
                })

            # 응답 구조를 작성하여 'parsed_text', 'float_text', 'category_label_predictions'를 포함시킵니다.
            result_response = {
                'parsed_text': parsed_text,
                'float_text': float_text,
                'category_label_predictions': category_label_predictions_list
            }
            # 결과를 반환합니다.
            return result_response
        else:
            raise Exception("Invalid result structure: 'outputs' not found")
    def parse_data(self, str_fields, float_fields):
        # 문자열 필드와 실수 필드를 고정된 순서대로 파싱
        parsed_text = ' || '.join([f"{field} : {str_fields[field]}" for field in str_fields.keys()])
        float_text =''
        # AMSL_AMT 값 추가
        if "AMSL_AMT" in float_fields:
            float_text += f"{float_fields['AMSL_AMT']}"
        return parsed_text,float_text

    def mcc_batch_classify(self, parsed_texts, float_texts):
        if not isinstance(parsed_texts, list) or not isinstance(float_texts, list):
            raise ValueError("Both parsed_texts and float_texts must be lists.")

        if len(parsed_texts) != len(float_texts):
            raise ValueError("parsed_texts and float_texts must have the same length.")

        batch_results = []
        total_count = len(parsed_texts)
        with grpc.insecure_channel(self.endpoint) as channel:
            stub = EC_grpc.MccEngineControllerStub(channel)
            #로그 출력 간격 설정
            log_interval = 100
            for idx, (text, floats) in enumerate(zip(parsed_texts, float_texts), start=1):
                try:
                    # float_text 변환
                    processed_floats = [float(f) for f in floats] if isinstance(floats, list) else [float(floats)]
                    request = MCC_proto.TextClassificationRequest(
                        text=text,
                        floats=processed_floats
                    )

                    # WrappedInference 메서드 호출
                    result = stub.WrappedInference(request)

                    # 결과 처리
                    if hasattr(result, 'outputs'):
                        category_label_predictions = result.outputs.category_label_predictions
                        category_label_predictions_list = [
                            {
                                'category': category.category,
                                'label_predictions': [
                                    {
                                        'label': label_prediction.label,
                                        'probability': label_prediction.probability
                                    }
                                    for label_prediction in category.label_predictions
                                ]
                            }
                            for category in category_label_predictions
                        ]

                        batch_results.append({
                            'parsed_text': text,
                            'float_text': floats,
                            'category_label_predictions': category_label_predictions_list
                        })
                    else:
                        batch_results.append({
                            'parsed_text': text,
                            'float_text': floats,
                            'error': "Invalid result structure: 'outputs' not found"
                        })
                except grpc.RpcError as e:
                    print(f"gRPC error for text: {text}, error: {e}")
                    batch_results.append({
                        'parsed_text': text,
                        'float_text': floats,
                        'error': str(e)
                    })

                # 진행률 로그 출력
                if idx % log_interval == 0 or idx == total_count:
                    logging.info(f"Processed {idx}/{total_count} items ({(idx / total_count) * 100:.2f}%)")
                    print(f"{(idx / total_count) * 100:.2f}")
        return batch_results

    def before_batch_db(self,hdqr_bob_dcd=None):
        print(hdqr_bob_dcd)
        print(type(hdqr_bob_dcd))
        
        with connection.cursor() as cursor:
            query = """
                SELECT 
                    TSTM_YMD, TSTM_NO, BRCD, HDQR_BOB_DCD, CDN, BDGT_TSTM_USE_HMS, 
                    AFST_NM, BZDY_YN, AMSL_AMT, TPBS_NM, AFST_BZN, AMSL_AFST_NO, 
                    AFST_TPBCD, AFST_DTL_ADR, BRNC_ADR, RCV_YMD, JOB_YMD, JOB_YN, 
                    FRRG_TS, FRRG_EMN, LSMD_TS, LSMD_EMN
                FROM CARD_INPUT_TEST
                WHERE JOB_YN = 'N' AND HDQR_BOB_DCD = %s
            """


            # 쿼리 실행 - 자리 표시자와 함께 값 전달
            cursor.execute(query, (hdqr_bob_dcd,))
            # 컬럼명 가져오기
            columns = [col[0] for col in cursor.description]
            
            # 튜플 결과를 딕셔너리로 변환
            data_to_classify = [
                dict(zip(columns, row)) for row in cursor.fetchall()
            ]
        
            # # 결과 가져오기
            # data_to_classify = cursor.fetchall()
            print(len(data_to_classify))
        # 고정된 순서에 따라 매핑
        mapping = {
        'BRCD': 'BRCD',
        'CDN': 'CDN',
        'BDGT_TSTM_USE_HMS': 'BDGT_TSTM_USE_HMS',
        'AFST_NM': 'AFST_NM',
        'TPBS_NM': 'TPBS_NM',
        'BZDY_YN': 'BZDY_YN',
        'AFST_DTL_ADR': 'AFST_DTL_ADR',
        'BRNC_ADR': 'BRNC_ADR',
        'AMSL_AMT': 'AMSL_AMT'
    }

        # 데이터 리스트 초기화
        str_fields_list = []
        float_fields_list = []
        results_with_pk = []
        # 데이터 처리
        for data in data_to_classify:
            # 문자열 필드와 실수 필드 분리
            str_fields = {key: data[key] for key in mapping.keys() if key != "AMSL_AMT"}
            float_fields = {"AMSL_AMT": data["AMSL_AMT"]}
            parsed_text, float_text = self.parse_data(str_fields, float_fields)

            # PK 정보 추가
            pk_info = {
            'TSTM_YMD': data['TSTM_YMD'],
            'TSTM_NO': data['TSTM_NO'],
            'BRCD': data['BRCD'],
            'HDQR_BOB_DCD': data['HDQR_BOB_DCD'],
            'FRRG_EMN': data['FRRG_EMN'],    # 쿼리에서 가져온 값 추가
            'LSMD_EMN': data['LSMD_EMN']     # 쿼리에서 가져온 값 추가
            }

            # 결과에 pk와 함께 추가
            results_with_pk.append({
                'PK': pk_info,
                'Input text': parsed_text,
                'Predictions': [],  # 예시로 'Predictions' 데이터를 넣음
            })

            # 리스트에 추가
            str_fields_list.append(parsed_text)
            float_fields_list.append(float_text)

        # 결과 반환
        return str_fields_list, float_fields_list, results_with_pk

    def after_batch_db(self, result_response, results_with_pk, model_id):
        print(len(result_response), len(results_with_pk))
        predictions = []

        # result_response의 각 항목을 처리
        for result in result_response:
            # 각 카테고리별 예측 결과를 처리
            category_predictions_list = []
            for category_info in result['category_label_predictions']:
                category = category_info['category']
                predictions_data = category_info['label_predictions']

                category_predictions = {
                    'Category': category,
                    'Predictions': [
                        {'Prediction': prediction['label'], 'Probability': prediction['probability'] * 100}
                        for prediction in predictions_data
                    ]
                }
                category_predictions_list.append(category_predictions)

            predictions.append(category_predictions_list)

        for result_entry, prediction in zip(results_with_pk, predictions):
            # prediction이 리스트라면, 각 항목은 카테고리별 예측값입니다.
            for category_predictions in prediction:
                category = category_predictions['Category']
                for pred in category_predictions['Predictions']:
                    category_predictions_parsed = {
                        'Category': category,
                        'Predictions': [
                            {'Prediction': pred['Prediction'], 'Probability': pred['Probability']}
                        ]
                    }
                    result_entry['Predictions'].append(category_predictions_parsed)

        from datetime import datetime
        # 오늘 날짜 계산 (다양한 형식)
        today_date = datetime.now().strftime('%Y%m%d')  # YYYYMMDD 형식 (연월일)
        today_year = datetime.now().strftime('%Y')      # YYYY 형식 (연도)
        today_year_month = datetime.now().strftime('%Y%m')  # YYYYMM 형식 (연월)
        print('check_point')
        # 결과를 데이터베이스에 삽입할 쿼리
        with connection.cursor() as cursor:
            insert_query = """
            INSERT INTO CARD_OUTPUT_TEST (TSTM_YMD, TSTM_NO, BRCD, HDQR_BOB_DCD, BDGT_ITEX_FRCS_CON, BDGT_BSNS_FRCS_CON, BDGT_PRFR_RSN_FRCS_CON, BDGT_ITEX_FRCS_PRB_CON, BDGT_BSNS_FRCS_PRB_CON, BDGT_PRFR_RSN_FRCS_PRB_CON, LEARNING_MODEL_ID, RSRE_YMD, RSRE_Y, RSRE_YM, FRRG_EMN, LSMD_EMN)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            ON DUPLICATE KEY UPDATE
                HDQR_BOB_DCD = VALUES(HDQR_BOB_DCD),
                BDGT_ITEX_FRCS_CON = VALUES(BDGT_ITEX_FRCS_CON),
                BDGT_BSNS_FRCS_CON = VALUES(BDGT_BSNS_FRCS_CON),
                BDGT_PRFR_RSN_FRCS_CON = VALUES(BDGT_PRFR_RSN_FRCS_CON),
                BDGT_ITEX_FRCS_PRB_CON = VALUES(BDGT_ITEX_FRCS_PRB_CON),
                BDGT_BSNS_FRCS_PRB_CON = VALUES(BDGT_BSNS_FRCS_PRB_CON),
                BDGT_PRFR_RSN_FRCS_PRB_CON = VALUES(BDGT_PRFR_RSN_FRCS_PRB_CON),
                LEARNING_MODEL_ID = VALUES(LEARNING_MODEL_ID),
                RSRE_YMD = VALUES(RSRE_YMD),
                RSRE_Y = VALUES(RSRE_Y),
                RSRE_YM = VALUES(RSRE_YM),
                FRRG_EMN = VALUES(FRRG_EMN),
                LSMD_EMN = VALUES(LSMD_EMN)
        """

            # 결과를 순회하면서 데이터베이스에 삽입
            for entry in results_with_pk:
                pk_info = entry['PK']
                # print(entry['Predictions'])
                # 각 카테고리별 데이터를 가공하여 변수에 저장
                BDGT_ITEX_FRCS_CON = self.parse_predictions(entry['Predictions'], 'BDMN_ITEX_MNGM_NO')
                BDGT_BSNS_FRCS_CON = self.parse_predictions(entry['Predictions'], 'BRCD-BDGT_BSNS_FRCS_CON')
                BDGT_PRFR_RSN_FRCS_CON = self.parse_predictions(entry['Predictions'], 'BDGT_PRFR_RSN_FRCS_CON')
                BDGT_ITEX_FRCS_PRB_CON = self.parse_probability(entry['Predictions'], 'BDMN_ITEX_MNGM_NO')
                BDGT_BSNS_FRCS_PRB_CON = self.parse_probability(entry['Predictions'], 'BRCD-BDGT_BSNS_FRCS_CON')
                BDGT_PRFR_RSN_FRCS_PRB_CON = self.parse_probability(entry['Predictions'], 'BDGT_PRFR_RSN_FRCS_CON')

                # print(pk_info)
                # print(f"BDGT_ITEX_FRCS_CON (예산관리비목관리번호): {BDGT_ITEX_FRCS_CON}")
                # print(f"BDGT_BSNS_FRCS_CON (예산관리비목관리번호_사업세부사업): {BDGT_BSNS_FRCS_CON}")
                # print(f"BDGT_PRFR_RSN_FRCS_CON (예산관리비목관리번호_예산집행사유코드): {BDGT_PRFR_RSN_FRCS_CON}")
                # print(f"BDGT_ITEX_FRCS_PRB_CON (예산관리비목관리번호): {BDGT_ITEX_FRCS_PRB_CON}")
                # print(f"BDGT_BSNS_FRCS_PRB_CON (예산관리비목관리번호_사업세부사업): {BDGT_BSNS_FRCS_PRB_CON}")
                # print(f"BDGT_PRFR_RSN_FRCS_PRB_CON (예산관리비목관리번호_예산집행사유코드): {BDGT_PRFR_RSN_FRCS_PRB_CON}")
                # print(f"RSRE_YMD :{today_date}")
                # print("---------------------")
                # 쿼리 실행
                cursor.execute(insert_query, (
                pk_info['TSTM_YMD'],
                pk_info['TSTM_NO'],
                pk_info['BRCD'],
                pk_info['HDQR_BOB_DCD'],
                BDGT_ITEX_FRCS_CON,
                BDGT_BSNS_FRCS_CON,
                BDGT_PRFR_RSN_FRCS_CON,
                BDGT_ITEX_FRCS_PRB_CON,
                BDGT_BSNS_FRCS_PRB_CON,
                BDGT_PRFR_RSN_FRCS_PRB_CON,
                model_id,
                today_date,       # RSRE_YMD (연월일)
                today_year,       # RSRE_Y (연도)
                today_year_month, # RSRE_YM (연월)
                pk_info['FRRG_EMN'],  # before_batch_db에서 가져온 값 사용
                pk_info['LSMD_EMN']   # before_batch_db에서 가져온 값 사용
            ))
            print(len(results_with_pk))
            # 변경 사항 커밋
            connection.commit()
        with connection.cursor() as cursor:
            # CARD_INPUT_TEST_TOP100 테이블의 JOB_YN 업데이트 쿼리 추가
            update_query = f"""
                        UPDATE CARD_INPUT_TEST AS input
                        JOIN CARD_OUTPUT_TEST AS output
                        ON input.TSTM_YMD = output.TSTM_YMD
                        AND input.TSTM_NO = output.TSTM_NO
                        AND input.BRCD = output.BRCD
                        SET input.JOB_YN = 'Y',
                        input.JOB_YMD = '{today_date}';
                    """


            # 업데이트 쿼리 실행
            cursor.execute(update_query)
            connection.commit()
        print("Data inserted successfully")

    def parse_predictions(self,predictions,category):
        result = []
        for pred_info in predictions:
            if pred_info['Category'] == category:
                result.extend([f"{pred['Prediction']}" for pred in pred_info['Predictions']])
        return '|'.join(result) if result else ''

    def parse_probability(self,predictions, category):
        result = []
        for pred_info in predictions:
            if pred_info['Category'] == category:
                result.extend([f"{pred['Probability']:.2f}" for pred in pred_info['Predictions']])
        return '|'.join(result) if result else ''

    def start_train(self, model_id, model_cfg, train_file, test_file):
        """학습 시작 전 모델 상태 확인 및 학습 시작"""
        logger.info(f"Checking status for model_id: {model_id}")
        
        # DB에서 모델 상태 확인
        with connection.cursor() as cursor:
        # 학습 중인 모델이 있는지 확인
            cursor.execute("SELECT id FROM LEARNING_MODEL WHERE DEPLOY_STATUS = 2")
            training_models = cursor.fetchall()  # 모든 학습 중인 모델을 가져옵니다.
        
            if training_models:
                # 학습 중인 모델이 있는 경우, 해당 모델의 상태를 학습 에러(4)로 변경
                for model in training_models:
                    cursor.execute(
                    "UPDATE LEARNING_MODEL SET DEPLOY_STATUS = 4 WHERE id = %s",
                    [model['id']]  # 모델 ID를 사용하여 상태 업데이트
                )
                logger.warning("There are models currently training. Cannot start a new training session.")
                return {
                    'code': 400,
                    'status': 'error',
                    'msg': 'There are models currently training. Please wait until they finish.',
                    'model_id': model_id
                }
            cursor.execute(
                "SELECT DEPLOY_STATUS FROM LEARNING_MODEL WHERE id = %s",
                [model_id]
            )
            result = cursor.fetchone()
            logger.info(f"DB query result for model {model_id}: {result}")
            
            if not result:
                raise Exception(f"Model ID {model_id} not found")
            
            deploy_status = result[0]
            logger.info(f"Current deploy status for model {model_id}: {deploy_status} (type: {type(deploy_status)})")
            
            # 학습 완료 상태(3) 체크
            if int(deploy_status) == 3:
                logger.warning(f"Model {model_id} is already trained (DEPLOY_STATUS: 3)")
                return {
                    'code': 400,
                    'status': 'error',  # status 필드 추가
                    'msg': f"Model {model_id} is already trained (DEPLOY_STATUS: 3),학습완료",  # message를 msg로 변경
                    'model_id': model_id
                }
        
        # 기존 학습 시작 로직
        try:
            user_learning_rate = float(model_cfg.get("learning_rate", 3))
            if user_learning_rate == int(user_learning_rate):
                learning_rate = int(user_learning_rate) * 1e-5
            else:
                learning_rate = 3e-5
        except (ValueError, TypeError):
            learning_rate = 3e-5

        # 요청 메시지 생성
        request = EC_proto.TrainingParams(
            model_name=str(model_id),
            train_file=train_file,
            dev_file=test_file,
            train_batch_size=int(model_cfg.get("batch_size", 16)),
            learning_rate=learning_rate,
            num_train_epochs=int(model_cfg.get("epochs", 10))
        )
        
        # gRPC 서버와 연결하여 요청
        with grpc.insecure_channel(self.endpoint) as channel:
            stub = EC_grpc.MccEngineControllerStub(channel)
            try:
                result = stub.StartTraining(request)

                # 현재 시간 생성
                current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')

                # 기존 배포 중인 모델을 배포 중단(7)으로 변경
                with connection.cursor() as cursor:
                    cursor.execute(
                    """UPDATE LEARNING_MODEL 
                       SET DEPLOY_STATUS = 7,
                           DEPLOY_STOP_DT = %s 
                       WHERE DEPLOY_STATUS = 5""",
                    [current_time]
                )
                    # 학습 시작 시 상태를 2(학습 중)로 변경
                    cursor.execute(
                        "UPDATE LEARNING_MODEL SET DEPLOY_STATUS = 2 WHERE id = %s",
                        [model_id]
                    )

                # 응답 형식 통일
                return {
                    'code': 200,
                    'status': 'success',
                    'msg': 'Training started successfully',
                    'model_id': model_id
                }
            except grpc.RpcError as e:
                logger.error(f"gRPC error: {e}")
                return {
                    'code': 500,
                    'status': 'error',
                    'msg': 'Failed to start training',
                    'model_id': model_id
                }

    def parse_train_test(self, file_name, default_dir):
        # 기본 폴더 경로가 존재하는지 확인하고, 없으면 생성
        os.makedirs(default_dir, exist_ok=True)

        # file_path 생성: default_dir에 file_name 추가
        file_path = os.path.join(default_dir, file_name)

        # 파일이 존재하는지 확인
        if not os.path.exists(file_path):
            raise FileNotFoundError(f"{file_path} 경로의 파일이 존재하지 않습니다.")

        # 파일 읽기
        with open(file_path, 'r', encoding='utf-8') as file:
            lines = file.readlines()

        # 첫 번째 라인(헤더) 분리
        header = lines[0]
        # 데이터 셔플 및 비율 설정
        data_lines = lines[1:]  # 헤더를 제외한 데이터
        random.shuffle(data_lines)
        train_ratio = 0.9
        train_size = int(len(data_lines) * train_ratio)
        train_data = data_lines[:train_size]
        test_data = data_lines[train_size:]

        file_name = file_name.split('.')[0]

        # 결과 파일 경로 설정: default_dir에 파일 이름 추가
        train_file_path = os.path.join(default_dir, f"{file_name}_train.txt")
        test_file_path = os.path.join(default_dir, f"{file_name}_test.txt")

        # 헤더를 포함하여 학습 데이터와 테스트 데이터 쓰기
        with open(train_file_path, 'w', encoding='utf-8') as train_file:
            train_file.write(header)  # 헤더 추가
            train_file.writelines(train_data)

        with open(test_file_path, 'w', encoding='utf-8') as test_file:
            test_file.write(header)  # 헤더 추가
            test_file.writelines(test_data)

        # 생성된 파일 경로 반환
        return train_file_path, test_file_path

    def stop_train(self,model_id):
        # 요청 메시지 생성 (필요시 요청 메시지를 정의)
        request = Empty()  # 여기서 Empty는 google.protobuf.empty_pb2에서 가져온 것입니다.

        # gRPC 서버와 연결하여 요청
        with grpc.insecure_channel(self.endpoint) as channel:
            stub = EC_grpc.MccEngineControllerStub(channel)  
            # # MccEngineControllerStub 사용
            try:
                result = stub.StopTraining(request)
                result_code = result.result_code
                result_msg = result.msg
            except grpc.RpcError as e:
                print(f"gRPC error: {e}")
                raise Exception("Failed to stop training")

        #학습중단 상태(4->10) 변경
        with connection.cursor() as cursor:
            if result_code == 0:  # 성공
                cursor.execute(
                    "UPDATE LEARNING_MODEL SET DEPLOY_STATUS = %s WHERE id = %s",
                    [10, model_id]  # 학습 중단 상태로 업데이트
                )
                return {
                    'code': 200,
                    'message': f"{model_id} 학습중지 성공",
                    'engine_result': result_msg,
                    'model_id': model_id
                }
            else:
                return {
                    'code': 500,
                    'message': f"{model_id} 학습중지 실패",
                    'engine_result': result_msg,
                    'model_id': model_id
                }
        return result

    def check_engine_status(self):
        # 모델 상태 체크 요청 메시지 생성
        request = EC_proto.ModelInformation()
        # gRPC 서버와 연결하여 요청
        with grpc.insecure_channel(self.endpoint) as channel:
            stub = EC_grpc.MccEngineControllerStub(channel)  # MccEngineControllerStub 사용
            try:
                result = stub.CheckEngineStatus(request)
                result_dict = {
                    'result_code': result.result_code,
                    'engine_status': result.engine_status,
                    'msg': result.msg,
                }
            except grpc.RpcError as e:
                print(f"gRPC error: {e}")
                raise Exception("Failed to check engine status")
        return result_dict

    def check_model_status(self):
        logging.info("DEBUG-1: Starting check_model_status")
        
        # DB에서 학습 중인 모델(DEPLOY_STATUS = 2) 가져오기
        with connection.cursor() as cursor:
            cursor.execute("SELECT id FROM LEARNING_MODEL WHERE DEPLOY_STATUS = 2 AND LEARNING_TYPE = 'CARD' LIMIT 1")
            training_model = cursor.fetchone()
            logging.info(f"DEBUG-2: Query result for training model: {training_model}")

        # 학습 중인 모델이 없는 경우
        if not training_model:
            logging.info("DEBUG-3: No training models found")
            return {'status': 'no_training_model', 'msg': 'No models are currently being trained.'}

        # 학습 중인 모델 ID 가져오기
        training_model_id = training_model[0]
        logging.info(f"DEBUG-4: Found training model ID: {training_model_id}")

        # 학습 상태를 gRPC로 확인
        training_request = EC_proto.ModelInformation(model_name=str(training_model_id))
        with grpc.insecure_channel(self.endpoint) as channel:
            stub = EC_grpc.MccEngineControllerStub(channel)
            try:
                training_result = stub.CheckModelStatus(training_request)
                logging.info(f"DEBUG-5: Training progress - {training_result.cur_step}/{training_result.total_steps}")

                # 학습이 완료된 경우
                if training_result.cur_step >= training_result.total_steps:
                    logging.info("DEBUG-6: Training completed, processing test results")
                    # 학습 완료 확인 후 540초 대기 (테스트 결과 생성 대기)
                    time.sleep(540)
                    training_result = stub.CheckModelStatus(training_request)
                    # 테스트 결과가 없거나 비정상적인 경우 상태를 4(학습 에러)로 변경
                    if not hasattr(training_result, 'test_results') or not training_result.test_results:
                        logging.warning("DEBUG-7: No test results found, updating status to ERROR (4)")
                        with connection.cursor() as cursor:
                            cursor.execute(
                                "UPDATE LEARNING_MODEL SET DEPLOY_STATUS = 4 WHERE id = %s",
                                [training_model_id]
                            )
                        return {
                            'status': 'error',
                            'msg': f"Model {training_model_id} training completed but no test results found.",
                        }
                    
                    logging.info(f"DEBUG-7: Test results found: {training_result.test_results}")
                    
                    # 카테고리별 컬럼 매핑
                    category_mapping = {
                        'BDMN_ITEX_MNGM_NO': 'BDGT_ITEX_FRCS_CON',
                        'BRCD-BDGT_BSNS_FRCS_CON': 'BDGT_BSNS_FRCS_CON',
                        'BDGT_PRFR_RSN_FRCS_CON': 'BDGT_PRFR_RSN_FRCS_CON'
                    }
                    
                    try:
                        with connection.cursor() as cursor:
                            for result in training_result.test_results:
                                if result.category in category_mapping:
                                    column_prefix = category_mapping[result.category]
                                    logging.info(f"DEBUG-8: Processing category {result.category} -> {column_prefix}")
                                    
                                    update_query = f"""
                                        UPDATE LEARNING_MODEL 
                                        SET DEPLOY_STATUS = 3,
                                            `{column_prefix}_F1_SCORE` = %s,
                                            `{column_prefix}_PRECISION` = %s,
                                            `{column_prefix}_RECALL` = %s
                                        WHERE id = %s
                                    """
                                    
                                    cursor.execute(update_query, [
                                        round(float(result.score_f1), 2),
                                        round(float(result.score_precision), 2),
                                        round(float(result.score_recall), 2),
                                        training_model_id
                                    ])
                                    logging.info(f"DEBUG-9: Updated metrics for {column_prefix}")
                            
                        connection.commit()
                        logging.info(f"DEBUG-10: All updates committed successfully")
                        
                        # 학습 데이터 폴더 삭제 (DB 업데이트 성공 후)
                        self.delete_training_data_folder(training_model_id)
                        logging.info(f"DEBUG-11: Attempted to delete training data folder for model {training_model_id}")

                        # 메트릭스 수집 부분 제거하고 바로 성공 상태 반환
                        return {
                            'status': 'success',
                            'msg': f"Model {training_model_id} training completed."
                        }
                    except Exception as e:
                        logging.error(f"DEBUG-ERROR: DB update failed - {str(e)}")
                        connection.rollback()
                        raise
                else:
                    logging.info("DEBUG-13: Training still in progress")
                    return {
                        'status': 'training_in_progress',
                        'msg': f"Model {training_model_id} is still training.",
                        'progress': {
                            'current_step': training_result.cur_step,
                            'total_steps': training_result.total_steps
                        }
                    }

            except grpc.RpcError as e:
                logging.error(f"DEBUG-ERROR: gRPC error - {str(e)}")
                raise Exception("Failed to check training model status")
    
    def delete_training_data_folder(self, model_id):
        """
        학습 완료된 모델의 학습 데이터 폴더를 삭제하는 함수
        
        Args:
            model_id: 모델 ID
        """
        try:
            # settings.py에 정의된 REAL_DATA_DIR 사용
            data_folder_path = os.path.join(settings.REAL_DATA_DIR, str(model_id))
            
            # 폴더가 존재하는지 확인
            if os.path.exists(data_folder_path):
                # 폴더 내 모든 파일 삭제
                for file_name in os.listdir(data_folder_path):
                    file_path = os.path.join(data_folder_path, file_name)
                    if os.path.isfile(file_path):
                        os.remove(file_path)
                        logging.info(f"Deleted file: {file_path}")
                
                # 폴더 삭제
                os.rmdir(data_folder_path)
                logging.info(f"Deleted training data folder for model {model_id}: {data_folder_path}")
            else:
                logging.warning(f"Training data folder not found for model {model_id}: {data_folder_path}")
        
        except Exception as e:
            logging.error(f"Error deleting training data folder for model {model_id}: {str(e)}")

    def replace_model(self, model_id, mem_id):
        logger.info(f"Replacing model with ID: {model_id} (type: {type(model_id)})")
        
        # 학습 중인 모델이 있는지 확인
        with connection.cursor() as cursor:
            cursor.execute("SELECT COUNT(*) FROM LEARNING_MODEL WHERE DEPLOY_STATUS = 2")
            training_count = cursor.fetchone()[0]
        
        if training_count > 0:
            with connection.cursor() as cursor:
                # 새로운 모델을 배포 실패(8) 상태로 변경
                cursor.execute(
                    "UPDATE LEARNING_MODEL SET DEPLOY_STATUS = 8 WHERE id = %s", [model_id]
                )
                # DEPLOY_HISTORY에 실패 기록 추가
                cursor.execute(
                    "INSERT INTO DEPLOY_HISTORY (MODEL_ID, EXEC_ID, RESULT) VALUES (%s, %s, %s)",
                    [model_id, mem_id, 'FAIL']
                )
            return {
                'status': 'error',
                'msg': '현재 학습 중인 모델이 있어 모델 교체를 진행할 수 없습니다.'
            }
        
        model_name = str(model_id)
        # 현재 시간 생성
        current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        
        # 배포 중인 이전 모델을 먼저 가져오기
        with connection.cursor() as cursor:
            cursor.execute("SELECT id FROM LEARNING_MODEL WHERE DEPLOY_STATUS = 5 LIMIT 1")
            pre_deploy_model = cursor.fetchone()

        # gRPC 서버와 연결하여 요청
        with grpc.insecure_channel(self.endpoint) as channel:
            stub = EC_grpc.MccEngineControllerStub(channel)
            try:
                request = EC_proto.ReplaceModelRequest(model_name=model_name)
                result = stub.ReplaceModel(request)
                
                # DB 상태 변경 처리
                if result.result_code == 0:  # 성공
                    with connection.cursor() as cursor:
                        cursor.execute(
                            """UPDATE LEARNING_MODEL 
                                SET DEPLOY_STATUS = 5,
                                DEPLOY_DT = %s 
                                WHERE id = %s""", 
                                [current_time, model_id]
                        )
                        if pre_deploy_model:
                            cursor.execute(
                                """UPDATE LEARNING_MODEL 
                                   SET DEPLOY_STATUS = 7,
                                       DEPLOY_STOP_DT = %s 
                                   WHERE id = %s""", 
                                [current_time, pre_deploy_model[0]]
                            )
                        # DEPLOY_HISTORY에 성공 기록 추가
                        cursor.execute(
                            "INSERT INTO DEPLOY_HISTORY (MODEL_ID, EXEC_ID, RESULT) VALUES (%s, %s, %s)",
                            [model_id, mem_id, 'SUCCESS']
                        )
                    return {'status': 'success', 'msg': result.msg}
                
                elif result.result_code == 1:  # 실패
                    with connection.cursor() as cursor:
                        # 새로운 모델을 배포 실패(8) 상태로 변경
                        cursor.execute(
                            "UPDATE LEARNING_MODEL SET DEPLOY_STATUS = 8 WHERE id = %s", [model_id]
                        )
                        # DEPLOY_HISTORY에 실패 기록 추가
                        cursor.execute(
                            "INSERT INTO DEPLOY_HISTORY (MODEL_ID, EXEC_ID, RESULT) VALUES (%s, %s, %s)",
                            [model_id, mem_id, 'FAIL']
                        )
                        # 이전 모델이 있는 경우 재배포 시도
                        if pre_deploy_model:
                            try:
                                previous_model_request = EC_proto.ReplaceModelRequest(
                                    model_name=str(pre_deploy_model[0])
                                )
                                logger.info(f"Attempting to re-deploy previous model: {pre_deploy_model[0]}")
                                previous_result = stub.ReplaceModel(previous_model_request)
                                
                                # 이전 모델 재배포 결과 처리
                                if (previous_result.msg == "The model is already loaded for inference" or 
                                    previous_result.msg.startswith('Successfully replaced')):
                                    # 이전 모델 재배포 성공 - 배포중(5) 상태 유지
                                    cursor.execute(
                                    """UPDATE LEARNING_MODEL 
                                       SET DEPLOY_STATUS = 5,
                                           DEPLOY_DT = %s 
                                       WHERE id = %s""", 
                                    [current_time, pre_deploy_model[0]]
                                )
                                    logger.info(f"Successfully re-deployed previous model: {pre_deploy_model[0]}")
                                else:
                                    # 이전 모델 재배포 실패 - 에러 로깅
                                    logger.error(f"Failed to re-deploy previous model. Result: {previous_result.msg}")
                                    raise Exception(f"Failed to re-deploy the previous model: {previous_result.msg}")
                                    
                            except grpc.RpcError as e:
                                logger.error(f"gRPC error while re-deploying previous model: {e}")
                                raise Exception(f"Failed to re-deploy the previous model: {str(e)}")
                    
                    return {
                        'status': 'failure', 
                        'msg': f"Model deployment failed: {result.msg}",
                        'model_id': model_id
                    }
                
            except grpc.RpcError as e:
                # gRPC 오류 발생 시 DEPLOY_HISTORY에 실패 기록 추가
                with connection.cursor() as cursor:
                    cursor.execute(
                        "INSERT INTO DEPLOY_HISTORY (MODEL_ID, EXEC_ID, RESULT) VALUES (%s, %s, %s)",
                        [model_id, mem_id, 'FAIL']
                    )
                logger.error(f"gRPC error: {e}")
                raise Exception("Failed to replace model")






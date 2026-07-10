from rest_framework.response import Response
from rest_framework.permissions import AllowAny
from rest_framework.decorators import api_view, permission_classes
from .service.multiservice import MultiServiceClient  # gRPC client import
from django.conf import settings
import os, sys
from .serializers import MccServiceSerializer, MccBatchServiceSerializer,StartTrainServiceSerializer, StopModelServiceSerializer, ReplaceModelServiceSerializer
from drf_yasg.utils import swagger_auto_schema
from drf_spectacular.utils import extend_schema
from django.db import connection

import logging
# 로깅 인스턴스 생성
logger = logging.getLogger(__name__)

# gRPC 서버 주소 설정
multi_service_client = MultiServiceClient(endpoint=settings.GRPC_ENDPOINT)
@swagger_auto_schema(method='post', request_body=MccServiceSerializer)
@api_view(['POST'])
@permission_classes([AllowAny])
def mcc_classify_service(request):
    req_body = request.data
    serializer = MccServiceSerializer(data=req_body)

    # 유효성 검사
    if not serializer.is_valid():
        # 유효성 검사 실패 시 오류 메시지 반환
        return Response({
            'code': 400,
            'message': '유효하지 않은 데이터',
            'errors': serializer.errors  # validation error 메시지
        }, status=400)

    # 유효한 데이터일 때
    model_id = serializer.validated_data["model_id"]
    str_fields = serializer.validated_data["str_fields"]
    float_fields = serializer.validated_data["float_fields"]
    # 모델 교체
    result_response = multi_service_client.replace_model(model_id)

    # 모델 교체 실패한 경우에는 즉시 Response 반환
    # print('replace check')
    if (result_response.get('message') != 'The model is already loaded for inference') and (not result_response.get('message').startswith('Successfully replaced')):
        return Response({
            'code': 500,
            'message': '모델 교체 실패',
            'result': result_response
        }, status=500)

    # 한글 매핑 정보
    # 'BRCD' : '브랜드 코드'
    # 'CDN' : '카테고리'
    # 'BDGT_TSTM_USE_HMS' : '예산 사용 시간'
    # 'AFST_NM' : '실행 이름'
    # 'TPBS_NM' : '매체 이름'
    # 'BZDY_YN' : '비즈니스 여부'
    # 'AFST_DTL_ADR' : '상세 주소'
    # 'BRNC_ADR' : '지점 주소'
    # 'AMSL_AMT' : '금액'

    parsed_text,float_text = multi_service_client.parse_data(str_fields, float_fields)
    try:
        result_response = multi_service_client.mcc_classify(parsed_text,float_text)
        # print(result_response)
        logger.info(f"분류 결과: {result_response}")
        return Response({
            'code': 200,
            'result': result_response
        })
    except Exception as e:
        logger.error(f"분류 중 오류 발생: {str(e)}")
        return Response({'code': 500, 'message': '서버 내부 오류'}, status=500)
@swagger_auto_schema(method='post', request_body=MccBatchServiceSerializer)
@api_view(['POST'])
@permission_classes([AllowAny])
def mcc_classify_batch_service(request):
    req_body = request.data
    serializer = MccBatchServiceSerializer(data=req_body)
    if not serializer.is_valid():
        # 유효성 검사 실패 시 오류 메시지 반환
        return Response({
            'code': 400,
            'message': '유효하지 않은 데이터',
            'errors': serializer.errors  # validation error 메시지
        }, status=400)
    model_id = serializer.validated_data["model_id"]
    hdqr_bob_dcd = serializer.validated_data['hdqr_bob_dcd']
    logger.info("배치 추론 요청이 수신됨")

    # 본부/영업점에 따라 데이터 처리
    str_fields_list, float_fields_list, results_with_pk = multi_service_client.before_batch_db(hdqr_bob_dcd=hdqr_bob_dcd)
    logger.info(f"데이터 길이 {len(str_fields_list)},{len(float_fields_list)}")

    # 모델 교체
    result_response = multi_service_client.replace_model(model_id)
    # 모델 교체 실패한 경우에만 즉시 Response 반환
    message = result_response.get('message')
    if message is not None and (message != 'The model is already loaded for inference') and (not message.startswith('Successfully replaced')):
        return Response({
            'code': 500,
            'message': '모델 교체 실패',
            'result': result_response
        }, status=500)

    logger.info(f"모델 교체 성공")
    try:
        logger.info("엔진 요청 완료")
        result_response = multi_service_client.mcc_batch_classify(str_fields_list, float_fields_list)
        logger.info("엔진 작업완료, db insert 요청")
        #분류후 후처리 db작업
        multi_service_client.after_batch_db(result_response, results_with_pk, model_id)
        logger.info(f"db insert 완료")
        return Response({
            'code': 200})
    except Exception as e:
        logger.error(f"추론 및 db insert 중 오류 발생: {str(e)}")
    return Response({'code': 500, 'message': '서버 내부 오류'}, status=500)

@swagger_auto_schema(method='post', request_body=StartTrainServiceSerializer)
@api_view(['POST'])
@permission_classes([AllowAny])
def start_train_service(request):
    logger.info("학습 시작 요청이 수신됨")

    data_dir = settings.DATA_DIR
    req_body = request.data
    serializer = StartTrainServiceSerializer(data=req_body)

    if serializer.is_valid():
        model_id = serializer.validated_data["model_id"]
        model_cfg = serializer.validated_data["model_cfg"]
        file_name = serializer.validated_data["file_name"]
    else:
        logger.warning("학습 요청 데이터가 유효하지 않음")
        return Response({'code': 400, 'message': '잘못된 요청 데이터'}, status=400)

    file_path = os.path.join(data_dir, str(model_id), file_name)
    model_path= os.path.join(data_dir, str(model_id))
    if not os.path.exists(file_path):
        logger.warning(f"파일 {file_name}이 {data_dir}에 존재하지 않음")
        return Response({'code': 404, 'message': f'{file_name}을 찾을 수 없음'}, status=404)

    try:
        train_file, test_file = multi_service_client.parse_train_test(file_path, model_path)
        logger.info(f"학습 데이터 성공적으로 파싱됨")
        result = multi_service_client.start_train(model_id, model_cfg, train_file, test_file)

        # 딕셔너리 응답 처리
        if result.get('code') == 400:  # 이미 학습 완료된 경우
            return Response({
                'code': result['code'],
                'message': result['msg']
            }, status=400)
        
        # 정상적인 학습 시작의 경우
        logger.info(f"학습이 성공적으로 시작됨: {result.get('msg')}")
        return Response({
            'code': result.get('code', 200),
            'message': result.get('msg', '학습이 시작되었습니다.')
        })

    except Exception as e:
        logger.error(f"훈련 중 오류 발생: {str(e)}")
        return Response({'code': 500, 'message': '서버 내부 오류'}, status=500)

@swagger_auto_schema(method='post', request_body=StopModelServiceSerializer)
@api_view(['POST'])
@permission_classes([AllowAny])
def stop_train_service(request):
    logger.info("훈련 중지 요청이 수신됨")
    req_body = request.data
    serializer = StopModelServiceSerializer(data=req_body)

    if serializer.is_valid():
        model_id = serializer.validated_data["model_id"]
        try:
            # gRPC 클라이언트 호출
            result_response = multi_service_client.stop_train(model_id)  # 모델 ID 전달

            return Response({
                'code': result_response['code'],
                'db_result': result_response['message'],  # DB 업데이트 결과
                'engine_result': result_response['engine_result'],  # 실제 응답 메시지
                'model_id': result_response['model_id']
            })
        except Exception as e:
            logger.error(f"훈련 중지 중 오류 발생: {str(e)}")
            return Response({'code': 500, 'message': '서버 내부 오류'}, status=500)
    else:
        logger.warning(f"잘못된 요청 데이터: {serializer.errors}")
        return Response(serializer.errors, status=400)

@api_view(['POST'])
@permission_classes([AllowAny])
def check_engine_status_service(request):
    logger.info("엔진 상태 요청이 수신됨")
    try:
        # gRPC 클라이언트 호출
        result_response = multi_service_client.check_engine_status()
        # logger.info(f"gRPC 호출 성공: {result_response}")
        return Response({
            'code': 200,
            'engine_result': result_response['msg'],  # 실제 응답 메시지
        })
    except Exception as e:
        logger.error(f"모델 상태 체크중 오류 발생: {str(e)}")
        return Response({'code': 500, 'message': '서버 내부 오류'}, status=500)

@api_view(['POST'])
@permission_classes([AllowAny])
def check_model_status_service(request):
    logger.info("모델 상태 요청이 수신됨")

    try:
        # gRPC 클라이언트 호출
        result_response = multi_service_client.check_model_status()

        # 상태에 따라 다른 응답을 클라이언트에 반환
        if result_response['status'] == 'success':
            logger.info(f"모델 상태 체크 성공: {result_response}")
            return Response({
                'code': 200,
                'status': 'success',
                'message': result_response['msg'],
                'data': result_response,  # gRPC 호출 결과
            })
        elif result_response['status'] == 'training_in_progress':
            logger.info(f"모델이 아직 학습 중: {result_response}")
            return Response({
                'code': 202,
                'status': 'training_in_progress',
                'message': result_response['msg'],
                'data': result_response,  # gRPC 호출 결과
            })
        elif result_response['status'] == 'no_training_model':
            logger.info(f"학습 중인 모델이 없음: {result_response}")
            return Response({
                'code': 204,
                'status': 'no_training_model',
                'message': result_response['msg'],
                'data': result_response,  # gRPC 호출 결과
            })
        else:
            logger.warning(f"알 수 없는 상태: {result_response}")
            return Response({
                'code': 400,
                'status': 'unknown_status',
                'message': 'Unknown status received from gRPC client.',
                'data': result_response,  # gRPC 호출 결과
            })

    except Exception as e:
        # 예외 처리 및 로깅
        logger.error(f"모델 상태 체크 중 오류 발생: {str(e)}")
        return Response({
            'code': 500,
            'status': 'error',
            'message': '서버 내부 오류가 발생했습니다.',
            'error': str(e),
        }, status=500)

@swagger_auto_schema(method='post', request_body=ReplaceModelServiceSerializer)
@api_view(['POST'])
@permission_classes([AllowAny])
def replace_model_service(request):
    logger.info("모델 교체 요청이 수신됨")
    req_body = request.data
    serializer = ReplaceModelServiceSerializer(data=req_body)
    if serializer.is_valid():
        model_id = str(serializer.validated_data["model_id"])
        mem_id = str(serializer.validated_data["mem_id"])
    try:
        # gRPC client 호출
        result_response = multi_service_client.replace_model(model_id,mem_id)
        return Response({
            'code': 200,
            'engine_result': result_response})
        # 실제 응답 메시지
    except Exception as e:
        print(f"Error during replacing model: {str(e)}")
        return Response({'code': 500, 'message': 'Internal server error'}, status=500)


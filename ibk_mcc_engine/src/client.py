# MindsLab Inc. 
# Multi-Category Classifier grpc client demo
import argparse, logging, time
import grpc
import proto.ibk_mcc_engine_control_pb2 as EC_proto
import proto.ibk_mcc_engine_control_pb2_grpc as EC_grpc
import proto.mcc_pb2 as MCC_proto
import proto.mcc_pb2_grpc as MCC_grpc
from google.protobuf.empty_pb2 import Empty

def request_check_engine_status(endpoint):
    with grpc.insecure_channel(endpoint) as channel:
        stub = EC_grpc.MccEngineControllerStub(channel)
        response = stub.CheckEngineStatus(Empty())

        return response

def request_start_training(endpoint, model_name, train_file, dev_file, 
                           train_batch_size, learning_rate, num_train_epochs):
    request = EC_proto.TrainingParams(model_name=model_name, train_file=train_file, dev_file=dev_file,
                                      train_batch_size=train_batch_size, learning_rate=learning_rate, 
                                      num_train_epochs=num_train_epochs)
    with grpc.insecure_channel(endpoint) as channel:
        stub = EC_grpc.MccEngineControllerStub(channel)
        response = stub.StartTraining(request)

        return response

def request_check_model_status(endpoint, model_name):
    request = EC_proto.ModelInformation(model_name=model_name)
    with grpc.insecure_channel(endpoint) as channel:
        stub = EC_grpc.MccEngineControllerStub(channel)
        response = stub.CheckModelStatus(request)

        return response

def request_stop_training(endpoint):
    with grpc.insecure_channel(endpoint) as channel:
        stub = EC_grpc.MccEngineControllerStub(channel)
        response = stub.StopTraining(Empty())

        return response

def request_replace_model(endpoint, model_name):
    request = EC_proto.ReplaceModelRequest(model_name=model_name)
    with grpc.insecure_channel(endpoint) as channel:
        stub = EC_grpc.MccEngineControllerStub(channel)
        response = stub.ReplaceModel(request)

        return response

def request_answer(endpoint, text, floats=None):
    request = MCC_proto.TextClassificationRequest(text=text, floats=floats)
    with grpc.insecure_channel(endpoint) as channel:
        stub = EC_grpc.MccEngineControllerStub(channel)
        response = stub.WrappedInference(request)

        return response

def _test_inference(endpoint, text, floats):
    print('Request CheckEngineStatus\n', request_check_engine_status(endpoint))
    print('Requesting mcc answer ON {}'.format(text))
    response = request_answer(endpoint, text, floats)
    print('Received the response.\n', response)
    print('Request CheckEngineStatus\n', request_check_engine_status(endpoint))

def _test_training(endpoint, model_name):
    print('Request CheckEngineStatus\n', request_check_engine_status(endpoint))
    print(request_start_training(endpoint,
                                 model_name=model_name,
                                 train_file='data/sample/debug.txt',
                                 dev_file='data/sample/debug.txt',
                                 train_batch_size=5,
                                 learning_rate=0.00005,
                                 num_train_epochs=2))

    print(f'Requesting CheckModelStatus {model_name}')
    print(request_check_model_status(endpoint, model_name))
    total_checks = 3
    for cur_check in range(total_checks):
        print(f'Sleep 30 seconds... {cur_check+1}/{total_checks}')
        time.sleep(30)
        print('Requesting CheckModelStatus')
        print(request_check_model_status(endpoint, model_name))
    print('Requesting StopTraining')
    print(request_stop_training(endpoint))
    print('Request CheckEngineStatus\n', request_check_engine_status(endpoint))

def _test_replace(endpoint):
    print('Requesting ReplaceModel with wrong model name')
    print(request_replace_model(endpoint, model_name='wrong_model'))
    print('Request CheckEngineStatus\n', request_check_engine_status(endpoint))

    print('Requesting ReplaceModel with unchanged model name')
    print(request_replace_model(endpoint, model_name='default_model'))
    print('Request CheckEngineStatus\n', request_check_engine_status(endpoint))

    print('Requesting ReplaceModel with a proper model name')
    print(request_replace_model(endpoint, model_name='debug_model'))
    print('Request CheckEngineStatus\n', request_check_engine_status(endpoint))


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("--port", type=int, default=36000)
    parser.add_argument('--model_name', type=str, default='default_model')
    parser.add_argument('--infer_text', type=str, default='mcc 테스트용 데이터입니다. 클라이언트 작동이 잘 되는지 확인하는 용도입니다. 잘 됐으면 좋겠다. 이미 잘된 것 같다.')
    parser.add_argument('--infer_float', dest='floats', type=float, action='append')

    args = parser.parse_args()
    return args

if __name__ == '__main__':
    args = parse_args()
    endpoint = 'localhost:{}'.format(args.port)

    if not args.floats:
        args.floats = [0.5, 0.2]

    _test_inference(endpoint, args.infer_text, args.floats)
    _test_training(endpoint, 'debug_model')
    _test_replace(endpoint)
    _test_inference(endpoint, args.infer_text, args.floats)

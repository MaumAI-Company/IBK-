# MindsLab Inc. 
# Multi Category Classifier grpc server

import argparse, logging, sys, time, grpc
from concurrent import futures
from datetime import datetime
from omegaconf import OmegaConf
from pathlib import Path

from runner import MccRunner, setup_config, setup_logger, logger

from proto.mcc_pb2 import MultiCategoryLabelResponse, CategoryLabelPrediction, LabelPrediction
from proto.mcc_pb2_grpc import MultiCategoryTextClassifierServicer, add_MultiCategoryTextClassifierServicer_to_server

_ONE_DAY_IN_SECONDS = 60 * 60 * 24

class MCC(MultiCategoryTextClassifierServicer):
    def __init__(self, cfg):
        self.runner = MccRunner(cfg)
        self.num_input_floats = self.runner.data_processor.input_floats_stat["num_input_floats"]

        logger.info('Warming up the GPU with a dummy data')
        input_floats = [0.0] * self.num_input_floats
        _ = self.runner.infer(['Dummy text for warmup'], input_floats=input_floats)
        logger.info('Done warming up')

    def close(self):
        del self.runner

    def GetMultiCategoryClassLabel(self, request, context):
        _input, top_k, input_floats = request.text, request.top_k, request.floats
        if len(input_floats) != self.num_input_floats:
            input_floats = self.runner.data_processor.input_floats_stat["mean"]

        results = self.runner.infer([_input], input_floats)
        logger.debug(results)

        response = self._postprocess(results[0], top_k)
        return response

    def _postprocess(self, result, top_k):
        category_preds = []
        for category, preds in result:
            predictions = [LabelPrediction(label=label, probability=prob) for label, prob in preds]
            category_pred = CategoryLabelPrediction(category=category, label_predictions=predictions[:top_k])
            category_preds.append(category_pred)

        output = MultiCategoryLabelResponse(category_label_predictions=category_preds)
        return output


# define and read arguments especially for running, not modeling
def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('--model_path', type=str, required=True)
    parser.add_argument('--log_level', type=str, default='INFO', choices=['INFO', 'DEBUG'])

    parser.add_argument('--port', type=int, required=True)

    args = parser.parse_args()
    return args


def main_process(cfg, port, kill_event=None, killed_event=None):
    logger.info('Initializing itc engine')
    mcc = MCC(cfg)

    logger.info('Building grpc server')
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1), )
    add_MultiCategoryTextClassifierServicer_to_server(mcc, server)

    server.add_insecure_port('[::]:{}'.format(port))
    server.start()

    logger.info('Mcc Server starting at 0.0.0.0:{}'.format(port))

    if kill_event:
        try:
            kill_event.wait()
            logger.info('Shutting down the xdc server')
            server.stop(3).wait()
            killed_event.set()
        except KeyboardInterrupt:
            logger.info('Shutting down the xdc server')
            server.stop(0)
        logger.info('Done the mcc server %d', port)
    else:
        try:
            while True:
                # Sleep forever, since `start` doesn't block
                time.sleep(_ONE_DAY_IN_SECONDS)
        except KeyboardInterrupt:
            logger.info('Shutting down the server')
            server.stop(0)


if __name__ == '__main__':
    args = parse_args()
    args.run = 'serve'

    cfg = setup_config(args)
    setup_logger(cfg)

    main_process(cfg, args.port)

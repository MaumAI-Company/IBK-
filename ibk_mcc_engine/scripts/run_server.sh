#!/bin/bash

# Generate proto
# bash scripts/build_proto.sh

export CUDA_VISIBLE_DEVICES=0,1

MODEL_PATH=${MODEL_PATH:-"/home/minds/ibk/mcc/ibk_file/models/902"}
PORT=${PORT:-36002}
INNER_PORT=${INNER_PORT:-36003}
LOG_LEVEL=${LOG_LEVEL:-"INFO"}

python src/engine_control_server.py \
    --model_path ${MODEL_PATH} \
    --port ${PORT} \
    --inner_port ${INNER_PORT} \
    --log_level ${LOG_LEVEL} \

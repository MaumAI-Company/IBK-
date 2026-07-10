#!/bin/bash
# export CUDA_VISIBLE_DEVICES=0

MODEL_PATH="models/1203_test"
FILE_PATH="/DATA/seonwoong/ibk/mcc_data/11월/1127/input_test_1219.csv"

python src/runner.py --run batch_infer \
    --model_path ${MODEL_PATH} \
    --file_path ${FILE_PATH}

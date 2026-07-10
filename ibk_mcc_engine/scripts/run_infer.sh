#!/bin/bash
#export CUDA_VISIBLE_DEVICES=5

MODEL_PATH="models/1203_test"

python src/runner.py --run infer \
    --model_path ${MODEL_PATH} \
    --infer_text "안녕하세요" \
    --infer_float 2 \
    --infer_float 4

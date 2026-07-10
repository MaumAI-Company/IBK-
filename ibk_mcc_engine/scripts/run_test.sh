#!/bin/bash
export CUDA_VISIBLE_DEVICES=0,1

#MODEL_PATH="/DATA/seonwoong/ibk/mcc_data/models/1st"
MODEL_PATH="/home/minds/ibk/mcc/ibk_file/models/0722_model2"
python src/runner.py --run test \
    --model_path ${MODEL_PATH} \

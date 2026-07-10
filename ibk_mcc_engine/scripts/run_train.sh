#!/bin/bash
export CUDA_VISIBLE_DEVICES=0,1

#CFG="cfg/config.yml"
CFG="cfg/config_floats.yml"
MODEL_CFG="cfg/model.yml"

python src/runner.py --run train \
    --cfg ${CFG} \
    --model_cfg ${MODEL_CFG} \

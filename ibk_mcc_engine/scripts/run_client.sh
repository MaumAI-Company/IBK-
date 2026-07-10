#!/bin/bash

PORT=36000

python src/client.py --port ${PORT} \
    --infer_text "테스트용 텍스트" \

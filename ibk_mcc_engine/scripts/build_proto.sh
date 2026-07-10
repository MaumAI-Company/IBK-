#!/bin/bash

PROTO_PARENT='src'
python -m grpc.tools.protoc --proto_path ${PROTO_PARENT} \
    "${PROTO_PARENT}/proto/mcc.proto" \
    --python_out ${PROTO_PARENT} \
    --grpc_python_out ${PROTO_PARENT} \
    
python -m grpc.tools.protoc --proto_path ${PROTO_PARENT} \
    "${PROTO_PARENT}/proto/ibk_mcc_engine_control.proto" \
    --python_out ${PROTO_PARENT} \
    --grpc_python_out ${PROTO_PARENT} \

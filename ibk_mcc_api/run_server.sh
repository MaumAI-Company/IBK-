#!/bin/bash

# 프로젝트 디렉토리로 이동
#cd ibk_mcc_django  # 실제 경로로 수정

# 서버 실행 (nohup으로 백그라운드에서 실행)
nohup python3 /root/ibk_mcc_django/manage.py runserver 0.0.0.0:9001 &
echo "Django 서버가 백그라운드에서 실행 중입니다. 로그는 logs/server.log 파일에서 확인할 수 있습니다."


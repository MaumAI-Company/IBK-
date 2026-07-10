 #!/bin/bash

# API URL (실제 서버 URL로 수정 필요)
API_URL="http://127.0.0.1:9001/mcc-classify-batch/"

# 모델 ID 설정
MODEL_ID="your_model_id"  # 실제 모델 ID로 변경 필요

# 본부 요청 (hdqr_bob_dcd: "1")
echo "Sending request for Headquarters..."
curl -X POST "$API_URL" \
     -H "Content-Type: application/json" \
     -d "{
         \"model_id\": "130",
         \"hdqr_bob_dcd\": \"1\"
     }"

# 영업점 요청 (hdqr_bob_dcd: "2")
echo "Sending request for Branch..."
curl -X POST "$API_URL" \
     -H "Content-Type: application/json" \
     -d "{
         \"model_id\": "130",
         \"hdqr_bob_dcd\": \"2\"
     }"

echo "Batch requests completed."

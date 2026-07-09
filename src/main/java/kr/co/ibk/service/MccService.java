package kr.co.ibk.service;

import kr.co.ibk.common.utils.HttpUtil;
import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.web.LearningModelInfo;
import kr.co.ibk.domain.web.MemberInfo;
import kr.co.ibk.repository.LearningModelRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MccService {

    private final HttpUtil httpUtil;
    @Value("${Globals.domain.card.mcc}")
    private String cardMccDomain;

    @Value("${Globals.domain.bill.mcc}")
    private String billMccDomain;

    private final LearningModelRepository learningModelRepository;
    private Logger log = LoggerFactory.getLogger(getClass());

    /* 0 : 등록 완료, 1 : 학습 데이터 생성 오류, 2 : 학습 중, 3 : 학습 완료, 4 : 학습 오류, 5 : 배포 중, 6 : 배포 완료, 7 : 배포 중지, 8 : 배포 실패, 9 : 학습 데이터 생성 중, 10 : 학습 중지*/

    /**
     * 학습 API
     * 해당옵션으로 학습
     *
     * @param modelId 모델 ID
     */
    public boolean trainModel(Integer modelId) {
        LearningModelInfo info = learningModelRepository.getLoad(modelId);
        if (info == null) {
            return false;
        }

        boolean result;
        try {
            JSONObject params = new JSONObject();
            params.put("model_id", modelId);
            JSONObject modelCfg = new JSONObject();
            modelCfg.put("learning_rate", info.getLearningRate());
            modelCfg.put("batch_size", info.getBatchSize());
            modelCfg.put("epochs", info.getEpoch());
            params.put("model_cfg", modelCfg);
            params.put("file_name", info.getFileName());

            result = sendPost("/train-model/", params, info.getLearningType());
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    /**
     * 학습 중지 API
     * 현재 학습 중인 모델을 중지
     *
     * @param modelId 모델 ID
     * @return {Boolean} 모델 중지 성공 여부를 나타내는 Boolean 값.
     *          -API 응답 responseBody의 "code" 값이 200이면 true, 그 외에는 false 반환
     */
    public Boolean stopModel(Integer modelId) {
        LearningModelInfo info = learningModelRepository.getLoad(modelId);
        if (info == null) {
            return false;
        }

        try {
            /*LearningModelForm form = new LearningModelForm();
            form.setId(modelId);
            form.setDeployStatus("0");
            learningModelRepository.updateStatus(form);*/

            JSONObject params = new JSONObject();
            params.put("model_id", modelId);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Boolean> future = executor.submit(() -> sendPost("/stop-model/", params, info.getLearningType()));

            Boolean result = future.get();
            executor.shutdown();
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 배포 API
     * 지정된 모델로 배포
     *
     * @param modelId 모델 ID
     * @return {Boolean} 모델 배포 성공 여부를 나타내는 Boolean 값.
     *          - API 응답 responseBody의 "code" 값이 200이면 true, 그 외에는 false 반환
     */
    public Boolean replaceModel(Integer modelId, MemberInfo memberInfo) {
        LearningModelInfo info = learningModelRepository.getLoad(modelId);
        if (info == null) {
            return false;
        }

        try {
            JSONObject params = new JSONObject();
            params.put("model_id", info.getId());
            params.put("mem_id", !ObjectUtils.isEmpty(memberInfo) ? memberInfo.getMemId() : null);
            params.put("model_name", info.getLearnName());

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<Boolean> future = executor.submit(() -> sendPost("/replace-model/", params, info.getLearningType()));

            Boolean result = future.get();
            executor.shutdown();
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * api 호출
     * @param path  호출할 API 경로
     * @param params 요청 바디(JSON 데이터)
     * @param learningType 모델 타입 (BC카드 / 세금계산서)에 따라 도메인 분기
     * @return {Boolean} API 응답 responseBody의 "code" 값이 200이면 true, 그 외에는 false 반환
     */
    private Boolean sendPost(String path, JSONObject params, LearningType learningType) {
        BufferedReader in = null;
        try {
            String domain;
            if (learningType.equals(LearningType.CARD)) {
                domain = cardMccDomain;
            } else if (learningType.equals(LearningType.BILL)) {
                domain = billMccDomain;
            } else {
                return false;
            }

            URL url = new URL(domain + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            log.info("Calling API: {} with params: {}", url.toString(), params.toString());

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = params.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            int codeValue = 0;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    codeValue = jsonObj.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            log.info("API response code: {}", codeValue);
            return responseCode == HttpURLConnection.HTTP_OK && codeValue == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e2) {
                // do nothing
            }
        }
    }
}

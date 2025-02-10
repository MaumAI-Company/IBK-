package kr.co.ibk.service;

import kr.co.ibk.common.utils.HttpUtil;
import kr.co.ibk.domain.enums.LearningType;
import kr.co.ibk.domain.web.LearningModelInfo;
import kr.co.ibk.repository.LearningModelRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    /* 0 : 등록 완료, 1 : 학습 데이터 생성 오류, 2 : 학습 중, 3 : 학습 완료, 4 : 학습 오류, 5 : 배포 중, 6 : 배포 완료, 7 : 배포 중지, 8 : 배포 실패 */

    /**
     * 학습 API
     * 해당옵션으로 학습
     * @param modelId
     */
    public void trainModel(Integer modelId) {
        LearningModelInfo info = learningModelRepository.getLoad(modelId);
        if (info == null) {
            return;
        }

        try {
            /*LearningModelForm form = new LearningModelForm();
            form.setId(modelId);
            form.setDeployStatus("3");
            learningModelRepository.updateStatus(form);*/

            JSONObject params = new JSONObject();
            params.put("model_id", modelId);
            JSONObject modelCfg = new JSONObject();
            modelCfg.put("learning_rate", info.getLearningRate());
            modelCfg.put("batch_size", info.getBatchSize());
            modelCfg.put("epochs", info.getEpoch());
            params.put("model_cfg", modelCfg);
            params.put("file_name", info.getFileName());
//            params.put("file_name", "test.txt");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendPost("/train-model", params, info.getLearningType());
                }
            }).start();
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 학습 중지 API
     * 학습중인 모델 중지
     * @param modelId
     */
    public void stopModel(Integer modelId) {
        LearningModelInfo info = learningModelRepository.getLoad(modelId);
        if (info == null) {
            return;
        }

        try {
            /*LearningModelForm form = new LearningModelForm();
            form.setId(modelId);
            form.setDeployStatus("0");
            learningModelRepository.updateStatus(form);*/

            JSONObject params = new JSONObject();
            params.put("model_id", modelId);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendPost("/stop-model", params, info.getLearningType());
                }
            }).start();
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 배포 API
     * 지정된 모델로 배포
     * @param modelId
     */
    public Boolean replaceModel(Integer modelId) {
        LearningModelInfo info = learningModelRepository.getLoad(modelId);
        if (info == null) {
            return false;
        }

        try {
            JSONObject params = new JSONObject();
            params.put("model_id", modelId);

            final Boolean[] result = {false};

            new Thread(new Runnable() {
                @Override
                public void run() {
                    result[0] = sendPost("/stop-model", params, info.getLearningType());
                }
            }).start();

            return result[0];
        } catch (Exception e) {
            return false;
        }
    }


    private Boolean sendPost(String path, JSONObject params, LearningType learningType) {
        BufferedReader in = null;
        try {
            String domain;
            if(learningType.equals(LearningType.CARD)) {
                domain = cardMccDomain;
            } else if(learningType.equals(LearningType.BILL)) {
                domain = billMccDomain;
            } else {
                return false;
            }

            URL url = new URL(domain + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = params.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            //System.out.println("url : " + url);
            //System.out.println("Response Code : " + responseCode);

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e2) {
                // do nothing
            }
        }
    }
}

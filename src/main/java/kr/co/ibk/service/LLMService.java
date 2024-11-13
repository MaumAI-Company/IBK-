package kr.co.ibk.service;

import kr.co.ibk.domain.web.LearningModelInfo;
import kr.co.ibk.model.LearningModelForm;
import kr.co.ibk.repository.LearningModelRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LLMService {

    @Value("${Globals.domain.llm}")
    private String llmDomain;

    private final LearningModelRepository learningModelRepository;

    /* 0: 등록완료, 1: 오류, 2: 학습완료 3: 학습중 4: 배포완료, 5: 롤백완료, 6: 배포중, 7:학습중지 */
    public void trainModel(Integer modelId) {
        LearningModelInfo info = learningModelRepository.getLoad(modelId);
        if (info == null) {
            return;
        }

        try {
            LearningModelForm form = new LearningModelForm();
            form.setId(modelId);
            form.setDeployStatus("3");
            learningModelRepository.updateStatus(form);

            JSONObject params = new JSONObject();
            params.put("model_id", modelId);
            JSONObject modelCfg = new JSONObject();
            modelCfg.put("learning_rate", info.getLearningRate());
            modelCfg.put("batch_size", info.getBatchSize());
            modelCfg.put("epochs", info.getEpoch());
            params.put("model_cfg", modelCfg);
            //params.put("file_name", info.getFileName());
            params.put("file_name", "test.txt");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendPost("/train-model", params);
                }
            }).start();
        } catch (Exception e) {
            return;
        }
    }

    public void stopModel(Integer modelId) {
        try {
            LearningModelForm form = new LearningModelForm();
            form.setId(modelId);
            form.setDeployStatus("0");
            learningModelRepository.updateStatus(form);

            JSONObject params = new JSONObject();
            params.put("model_id", modelId);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendPost("/stop-model", params);
                }
            }).start();
        } catch (Exception e) {
            return;
        }
    }

    private String sendPost(String path, JSONObject params) {
        BufferedReader in = null;
        try {
            URL url = new URL(llmDomain + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = params.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code : " + responseCode);

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
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

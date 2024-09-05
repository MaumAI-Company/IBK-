package com.mindslab.web.common.support.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * API Server 사용시 참고용 코드
 *
 */
@Component
@Slf4j
public class ApiClientUtils {

    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    private String getApiServerUrl(String domain) {
        return getApiServerUrl(domain, 80, "");
    }

    private String getApiServerUrl(String domain, int port) {
        return getApiServerUrl(domain, port, "");
    }

    private String getApiServerUrl(String domain, int port, String mappingUrl) {
        return "http://" + domain + ":" + port + mappingUrl;
    }

    public Map<String, Object> callApi(String domain, int port, String mappingUrl, HttpMethod method,
            Map<String, Object> params) {

        HashMap<String, Object> result = new HashMap<String, Object>();
        HttpHeaders headers = new HttpHeaders();
        String url = getApiServerUrl(domain, port, mappingUrl);
        String body = null;
        ResponseEntity<String> resultString = null;

        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        if (HttpMethod.GET.equals(method)) {
            // 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
            builder.build(false);// 자동인코딩방지
            resultString = restTemplate.exchange(builder.toUriString(), method, new HttpEntity<>(headers),
                    String.class);
        } else {

            HttpEntity<?> entity = null;
            try {
                body = objectMapper.writeValueAsString(params);
            } catch (IOException e) {
            }

            if (body != null) {
                entity = new HttpEntity<>(body, headers);
            } else {
                entity = new HttpEntity<>(headers);
            }
            // 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
            resultString = restTemplate.exchange(url, method, entity, String.class);
        }
        result.put("statusCode", resultString.getStatusCodeValue()); // http status code를 확인
        result.put("header", resultString.getHeaders()); // 헤더 정보 확인
        result.put("body", resultString.getBody()); // 실제 데이터 정보 확인
        // log.debug("resultString.getBody() :: " + resultString.getBody());

        // 데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
        ObjectMapper mapper = new ObjectMapper();
        String bodyJson = "";
        List<HashMap<String, Object>> bodyList = null;
        try {
            bodyJson = mapper.writer().writeValueAsString(resultString.getBody());
            bodyList = mapper.readValue(
                    resultString.getBody(), new TypeReference<ArrayList<HashMap<String, Object>>>() {
                    });
        } catch (JsonProcessingException e) {
        }
        result.put("bodyJson", bodyJson); // 실제 데이터 정보 JSON 형식으로 반환
        result.put("bodyList", bodyList); // 실제 데이터 정보 JSON 형식으로 반환

        return result;

    }

    /**
     * 보낼 메시지 크기가 큰 경우, 혹은 한글이 포함된 경우 POST로 보내는 것이 안전합니다.
     *
     * @param domain      애플리케이션에 할당된 도메인
     * @param port        API Port 번호
     * @param channelName 이벤트와 메시지를 보낼 채널
     * @param event       이벤트
     * @param message     메시지
     */
    public void sendByPost(String domain, int port, String mappingUrl, Map<String, Object> params) {
        String serverUrl = getApiServerUrl(domain, port);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(params);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (body != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            HttpEntity entity = new HttpEntity(body, headers);

            restTemplate.postForEntity(
                    serverUrl,
                    entity,
                    String.class);
        }
    }

    /**
     * 메시지가 짧거나 한글 메시지가 아닌 경우라면 GET으로도 보낼 수 있습니다.
     *
     * @param domain      애플리케이션에 할당된 도메인
     * @param port        API Port 번호
     * @param channelName 이벤트와 메시지를 보낼 채널
     * @param event       이벤트
     * @param message     메시지
     */
    public void sendByGet(String domain, int port, String mappingUrl, Map<String, Object> params) {
        String serverUrl = getApiServerUrl(domain, port);

        restTemplate.getForObject(
                serverUrl,
                String.class,
                params);
    }

}
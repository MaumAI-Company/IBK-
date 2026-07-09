package com.mindslab.web.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindslab.WebApplication;
import com.mindslab.web.common.support.utils.ApiClientUtils;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = WebApplication.class, properties = "spring.profiles.active:local")
@ActiveProfiles("local")
@Slf4j
public class RestTestControllerTest {

    @Autowired
    private ApiClientUtils apiClientUtils;

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private JSONObject jsonObject;
    private ObjectMapper objectMapper;

    //@Test
    public void test_getForObject() throws Exception {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        jsonObject = new JSONObject();
        jsonObject.put("str", "테스트");

        String URI = "";

        // URI = "http://localhost:8080/rest/test";
        URI = "http://localhost:8080/rest/test";

        String params = jsonObject.toString();
        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);

        String resultJsonStr = restTemplate.postForObject(URI, request, String.class);
        log.debug("::::: " + (resultJsonStr));

    }

    //@Test
    public void test_getExchage() throws Exception {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        String jsonInString = "";

        HashMap<String, Object> result = new HashMap<String, Object>();

        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json";

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(url + "?" + "key=430156241533f1d058c603178cc3ca0e&targetDt=20120101").build();

        // 이 한줄의 코드로 API를 호출해 MAP타입으로 전달 받는다.
        ResponseEntity<Map> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, Map.class);
        result.put("statusCode", resultMap.getStatusCodeValue()); // http status code를 확인
        result.put("header", resultMap.getHeaders()); // 헤더 정보 확인
        result.put("body", resultMap.getBody()); // 실제 데이터 정보 확인
        // 데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
        ObjectMapper mapper = new ObjectMapper();
        jsonInString = mapper.writeValueAsString(resultMap.getBody());

        log.debug("::::: " + (result.toString()));
        log.debug("");
    }

    //@Test
    public void test_callApi() throws Exception {

        String domain = "jsonplaceholder.typicode.com";
        int port = 80;
        String mappingUrl = "/posts";
        HashMap<String, Object> params = null;

        domain = "localhost";
        port = 8080;
        mappingUrl = "/rest/testGET";
        params = new HashMap<String, Object>();

        params.put("str", "테스트");
        params.put("str1", "테스트1");
        params.put("str2", "테스트2");
        params.put("str3", "테스트3");

        HashMap<String, Object> result = (HashMap<String, Object>) apiClientUtils.callApi(domain,
                port,
                mappingUrl,
                HttpMethod.GET, params);

        // 데이터를 제대로 전달 받았는지 확인 string형태로 파싱해줌
        // ObjectMapper mapper = new ObjectMapper();
        // jsonInString = mapper.writeValueAsString(resultMap.getBody());

        log.debug("::::: " + (result.toString()));
        log.debug("");

    }

}

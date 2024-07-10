package com.mindslab.web.config;

import java.io.IOException;
import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setReadTimeout(5000); // read timeout
        factory.setConnectTimeout(3000); // connection timeout

        // Apache HttpComponents HttpClient
        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(50)// 최대 커넥션 수
                .setMaxConnPerRoute(20).build(); // 각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        factory.setHttpClient(httpClient);

        /*
         * # 로깅 사용 안할 시
         * RestTemplate restTemplate = new RestTemplate(factory);
         * return restTemplate;
         */
        // # 로깅 사용 시
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return restTemplate;
    }
}

@Slf4j
class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        // 전
        log.info(":: Before ::");
        ClientHttpResponse response = execution.execute(request, body);
        // 후
        log.info(":: After ::");

        return response;

    }
}

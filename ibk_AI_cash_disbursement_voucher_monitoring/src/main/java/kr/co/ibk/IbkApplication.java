package kr.co.ibk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;

import java.nio.charset.Charset;

@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class IbkApplication {

    public static void main(String[] args) {
        SpringApplication.run(IbkApplication.class, args);
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }
}

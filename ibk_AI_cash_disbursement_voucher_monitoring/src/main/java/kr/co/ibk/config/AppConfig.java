package kr.co.ibk.config;

import kr.co.ibk.common.exceptions.ValidCustomException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                Throwable error = getError(webRequest);
                if (error instanceof ValidCustomException) {
                    errorAttributes.put("errors", ((ValidCustomException)error).getErrors());
                }
                return errorAttributes;
            }
        };
    }

}

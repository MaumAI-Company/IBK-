package com.mindslab.web.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@EnableConfigurationProperties
@ConfigurationProperties()
@PropertySources({@PropertySource(value = {"classpath:/custom-${spring.profiles.active}.properties"}, ignoreResourceNotFound = true)})
@Data
public class MidsLabCustomProperties {
    private Custom custom;
    private String name;
    @Data
    public static class Custom {
        String name;
    }
}

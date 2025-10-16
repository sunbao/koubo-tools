package com.koubotools.scriptgenerator.config;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiConfig {
    
    @Value("${dashscope.api.key}")
    private String dashscopeApiKey;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public Generation qwenGeneration() {
        return new Generation();
    }
    
    @Value("${dashscope.model.name}")
    private String dashscopeModelName;
    
    @Bean
    public QwenParam qwenParam() {
        return QwenParam.builder()
                .model(dashscopeModelName)
                .build();
    }
}
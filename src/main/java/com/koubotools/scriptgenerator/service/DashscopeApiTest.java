package com.koubotools.scriptgenerator.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.TokenUnavailableException;
import com.alibaba.dashscope.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class DashscopeApiTest {
    
    @Value("${dashscope.api.key}")
    private String dashscopeApiKey;
    
    @Value("${dashscope.model.name:qwen-turbo}")
    private String modelName;
    
    private Generation generation = new Generation();
    
    /**
     * 测试阿里百炼平台API连接
     */
    public boolean testApiConnection() {
        try {
            // 设置API密钥
            Constants.apiKey = dashscopeApiKey;
            
            // 构建测试消息
            Message message = Message.builder()
                    .role(Role.USER.getValue())
                    .content("你好，请简单介绍一下自己")
                    .build();
            
            // 构建参数
            QwenParam param = QwenParam.builder()
                    .model(modelName)
                    .messages(List.of(message))
                    .resultFormat(QwenParam.ResultFormat.MESSAGE)
                    .topP(0.8)
                    .temperature(0.7)
                    .maxTokens(100)
                    .build();
            
            // 调用API
            GenerationResult result = generation.call(param);
            
            // 输出结果
            String response = result.getOutput().getChoices().get(0).getMessage().getContent();
            System.out.println("阿里百炼平台API测试成功，响应内容: " + response);
            
            return true;
        } catch (ApiException | TokenUnavailableException | InputRequiredException e) {
            System.err.println("阿里百炼平台API测试失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("阿里百炼平台API测试出现未知错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
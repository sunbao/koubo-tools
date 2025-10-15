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
import com.koubotools.scriptgenerator.model.Hotspot;
import com.koubotools.scriptgenerator.model.Script;
import com.koubotools.scriptgenerator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AiScriptGenerationService {
    
    @Value("${dashscope.api.key}")
    private String dashscopeApiKey;
    
    @Value("${dashscope.model.name:qwen-turbo}")
    private String modelName;
    
    @Autowired
    private Generation generation;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 根据热点和用户配置生成文案
     * @param user 用户信息
     * @param hotspot 热点信息
     * @return 生成的文案
     */
    public Script generateScript(User user, Hotspot hotspot) {
        // 构建AI提示词
        String prompt = buildPrompt(user, hotspot);
        
        // 调用AI服务生成文案
        String generatedContent = callAiService(prompt);
        
        // 创建并返回文案对象
        Script script = new Script();
        script.setTitle(hotspot.getTitle());
        script.setContent(generatedContent);
        script.setPlatform(user.getPlatformSettings());
        script.setHotReference(hotspot.getHotId());
        
        return script;
    }
    
    /**
     * 构建AI提示词
     */
    private String buildPrompt(User user, Hotspot hotspot) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个专业的短视频口播文案创作者，请根据以下信息创作一段吸引人的口播文案:\n\n");
        prompt.append("主题: ").append(hotspot.getTitle()).append("\n");
        prompt.append("内容要点: ").append(hotspot.getContent()).append("\n");
        prompt.append("创作风格: ").append(user.getAccountStyle()).append("\n");
        prompt.append("目标平台: ").append(user.getPlatformSettings()).append("\n");
        prompt.append("口播类型: ").append(user.getBroadcastType()).append("\n\n");
        prompt.append("要求:\n");
        prompt.append("1. 文案长度控制在150-200字左右\n");
        prompt.append("2. 语言要口语化，适合口播\n");
        prompt.append("3. 开头要有吸引力，能抓住观众注意力\n");
        prompt.append("4. 内容要有价值，能给观众带来收获\n");
        prompt.append("5. 结尾要有互动性，引导观众点赞评论\n\n");
        prompt.append("请直接输出文案内容，不要添加其他说明:");
        
        return prompt.toString();
    }
    
    /**
     * 调用AI服务生成文案
     * 使用阿里的通义千问模型
     */
    private String callAiService(String prompt) {
        try {
            // 设置API密钥
            Constants.apiKey = dashscopeApiKey;
            
            // 构建消息
            Message message = Message.builder()
                    .role(Role.USER.getValue())
                    .content(prompt)
                    .build();
            
            // 构建参数
            QwenParam param = QwenParam.builder()
                    .model(modelName)
                    .messages(List.of(message))
                    .resultFormat(QwenParam.ResultFormat.MESSAGE)
                    .topP(0.8)
                    .temperature(0.7)
                    .maxTokens(512)
                    .build();
            
            // 调用通义千问API
            GenerationResult result = generation.call(param);
            
            // 提取生成的文本
            String generatedText = result.getOutput().getChoices().get(0).getMessage().getContent();
            return generatedText;
        } catch (ApiException | TokenUnavailableException | InputRequiredException e) {
            System.err.println("调用通义千问API失败: " + e.getMessage());
            // 返回默认文案
            return "这是一个由通义千问AI生成的示例口播文案，基于热点话题：" + prompt.substring(0, Math.min(prompt.length(), 30)) + "...";
        } catch (Exception e) {
            System.err.println("AI服务调用失败: " + e.getMessage());
            // 返回默认文案
            return "这是一个由AI生成的示例口播文案，基于热点话题。内容非常精彩，值得一看！";
        }
    }
    
    /**
     * 评估文案质量
     */
    public double evaluateScriptQuality(Script script) {
        // 简单的质量评估实现
        // 实际项目中可以使用更复杂的算法或AI模型进行评估
        String content = script.getContent();
        if (content == null || content.isEmpty()) {
            return 0.0;
        }
        
        int length = content.length();
        
        // 基于长度的简单评分
        if (length > 100 && length < 300) {
            return 0.8; // 长度适中，得分较高
        } else if (length > 50 && length <= 100) {
            return 0.6; // 长度较短，得分中等
        } else if (length > 300 && length <= 500) {
            return 0.7; // 长度较长，得分中等
        } else {
            return 0.5; // 长度不合适，得分较低
        }
    }
    
    /**
     * 优化文案质量
     */
    public Script optimizeScript(Script script) {
        // 简单的文案优化实现
        // 实际项目中可以使用AI进行更复杂的优化
        
        String content = script.getContent();
        if (content == null || content.isEmpty()) {
            return script;
        }
        
        // 确保文案有开头、主体和结尾
        if (!content.contains("大家好") && content.length() > 50) {
            content = "大家好！" + content;
        }
        
        if (!content.endsWith("！") && !content.endsWith("？")) {
            content += "你觉得怎么样？欢迎在评论区分享你的看法！";
        }
        
        script.setContent(content);
        return script;
    }
}
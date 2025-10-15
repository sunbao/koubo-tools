package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.Hotspot;
import com.koubotools.scriptgenerator.model.Script;
import com.koubotools.scriptgenerator.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiScriptGenerationServiceTest {

    @Autowired
    private AiScriptGenerationService aiScriptGenerationService;

    @Test
    void testGenerateScript() {
        // 创建测试用户
        User user = new User("testUser", "娱乐");
        user.setAccountStyle("幽默风趣");
        user.setPlatformSettings("抖音");

        // 创建测试热点
        Hotspot hotspot = new Hotspot("测试热点", "这是一个测试热点内容", "抖音", 95.5);

        // 生成文案
        Script script = aiScriptGenerationService.generateScript(user, hotspot);

        // 验证结果
        assertNotNull(script);
        assertNotNull(script.getTitle());
        assertNotNull(script.getContent());
        assertEquals("抖音", script.getPlatform());
        assertTrue(script.getContent().length() > 0);
    }

    @Test
    void testEvaluateScriptQuality() {
        // 创建测试文案
        Script script = new Script("测试标题", "这是一个测试文案内容，长度适中，适合评估质量。", "抖音");

        // 评估质量
        double quality = aiScriptGenerationService.evaluateScriptQuality(script);

        // 验证结果
        assertTrue(quality > 0);
        assertTrue(quality <= 1);
    }
    
    @Test
    void testOptimizeScript() {
        // 创建测试文案
        Script script = new Script("测试标题", "这是测试内容", "抖音");
        
        // 优化文案
        Script optimizedScript = aiScriptGenerationService.optimizeScript(script);
        
        // 验证结果
        assertNotNull(optimizedScript);
        assertNotNull(optimizedScript.getContent());
        assertTrue(optimizedScript.getContent().length() >= script.getContent().length());
    }
}
package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.Script;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeleprompterServiceTest {

    @Autowired
    private TeleprompterService teleprompterService;

    @Test
    void testCalculateOptimalSpeed() {
        // 测试不同长度的内容
        int speed1 = teleprompterService.calculateOptimalSpeed("短内容");
        int speed2 = teleprompterService.calculateOptimalSpeed("这是一个中等长度的内容，用来测试语速计算功能。");
        int speed3 = teleprompterService.calculateOptimalSpeed("这是一个很长很长的内容，用来测试语速计算功能。内容需要足够长才能测试不同的分支条件，所以我们需要添加更多的文字来确保长度超过300个字符。这样我们就可以验证长内容的语速计算是否正确。");
        
        // 验证结果
        assertTrue(speed1 > 0);
        assertTrue(speed2 > 0);
        assertTrue(speed3 > 0);
        
        // 验证语速逻辑（短内容较快，长内容较慢）
        assertTrue(speed1 >= speed2);
        assertTrue(speed2 >= speed3);
    }
}
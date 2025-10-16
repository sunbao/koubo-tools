package com.koubotools.scriptgenerator.controller;

import com.koubotools.scriptgenerator.service.DashscopeApiTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class ApiTestController {
    
    @Autowired
    private DashscopeApiTest dashscopeApiTest;
    
    @GetMapping("/dashscope")
    public ResponseEntity<String> testDashscopeApi() {
        boolean success = dashscopeApiTest.testApiConnection();
        if (success) {
            return ResponseEntity.ok("阿里百炼平台API连接测试成功！");
        } else {
            return ResponseEntity.status(500).body("阿里百炼平台API连接测试失败，请检查配置！");
        }
    }
}
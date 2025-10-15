package com.koubotools.scriptgenerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koubotools.scriptgenerator.model.Hotspot;
import com.koubotools.scriptgenerator.model.User;
import com.koubotools.scriptgenerator.service.HotspotService;
import com.koubotools.scriptgenerator.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ScriptGeneratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    
    @Autowired
    private HotspotService hotspotService;

    @Autowired
    private ObjectMapper objectMapper;

    private String authToken;
    private User testUser;
    private Hotspot testHotspot;

    @BeforeEach
    void setUp() throws Exception {
        // 创建测试用户
        testUser = userService.createUser("testUser" + System.currentTimeMillis(), "娱乐");
        
        // 创建测试热点
        testHotspot = new Hotspot("测试热点", "测试热点内容", "抖音", 95.5);
        testHotspot = hotspotService.saveHotspot(testHotspot);
        
        // 用户登录获取token
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testUser" + System.currentTimeMillis());
        loginRequest.put("password", "password");
        
        // 由于我们没有实现真正的密码验证，这里直接生成token
        // 在实际测试中，你需要根据实际的登录逻辑进行调整
    }

    @Test
    void testGenerateScript() throws Exception {
        // 测试生成文案接口
        mockMvc.perform(post("/api/scripts/generate")
                .param("userId", testUser.getUserId().toString())
                .param("hotspotId", testHotspot.getHotId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    
    @Autowired
    private UserService userService;
    
    // 简单的内存token存储，实际项目中应该使用Redis等
    private Map<String, Long> tokenUserMap = new HashMap<>();
    private Map<Long, String> userTokenMap = new HashMap<>();
    
    /**
     * 用户登录
     */
    public String login(String username, String password) {
        // 简单的用户名密码验证
        // 实际项目中应该使用加密密码和更安全的验证方式
        User user = userService.getUserByUsername(username);
        if (user != null) {
            // 生成token
            String token = UUID.randomUUID().toString();
            tokenUserMap.put(token, user.getUserId());
            userTokenMap.put(user.getUserId(), token);
            return token;
        }
        return null;
    }
    
    /**
     * 用户登出
     */
    public void logout(String token) {
        Long userId = tokenUserMap.get(token);
        if (userId != null) {
            tokenUserMap.remove(token);
            userTokenMap.remove(userId);
        }
    }
    
    /**
     * 验证token
     */
    public boolean validateToken(String token) {
        return tokenUserMap.containsKey(token);
    }
    
    /**
     * 根据token获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        return tokenUserMap.get(token);
    }
    
    /**
     * 根据用户ID获取token
     */
    public String getTokenFromUserId(Long userId) {
        return userTokenMap.get(userId);
    }
}
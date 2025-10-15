package com.koubotools.scriptgenerator.controller;

import com.koubotools.scriptgenerator.model.User;
import com.koubotools.scriptgenerator.service.UserService;
import com.koubotools.scriptgenerator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String broadcastType) {
        // 检查用户名是否已存在
        User existingUser = userService.getUserByUsername(username);
        if (existingUser != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }
        
        // 创建用户
        User user = userService.createUser(username, broadcastType);
        
        // 自动登录并生成token
        String token = authService.login(username, password);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        String token = authService.login(username, password);
        if (token != null) {
            User user = userService.getUserByUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            authService.logout(token);
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(HttpServletRequest request, @PathVariable Long id, @RequestBody User user) {
        // 验证用户权限
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!currentUserId.equals(id)) {
            return ResponseEntity.status(403).build();
        }
        
        user.setUserId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request, @PathVariable Long id) {
        // 验证用户权限
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!currentUserId.equals(id)) {
            return ResponseEntity.status(403).build();
        }
        
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/account-style")
    public ResponseEntity<User> updateAccountStyle(HttpServletRequest request, @PathVariable Long id, @RequestBody String accountStyle) {
        // 验证用户权限
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!currentUserId.equals(id)) {
            return ResponseEntity.status(403).build();
        }
        
        User user = userService.updateAccountStyle(id, accountStyle);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/preferences")
    public ResponseEntity<User> updatePreferences(HttpServletRequest request, @PathVariable Long id, @RequestBody String preferences) {
        // 验证用户权限
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!currentUserId.equals(id)) {
            return ResponseEntity.status(403).build();
        }
        
        User user = userService.updatePreferences(id, preferences);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/platform-settings")
    public ResponseEntity<User> updatePlatformSettings(HttpServletRequest request, @PathVariable Long id, @RequestBody String platformSettings) {
        // 验证用户权限
        Long currentUserId = (Long) request.getAttribute("userId");
        if (!currentUserId.equals(id)) {
            return ResponseEntity.status(403).build();
        }
        
        User user = userService.updatePlatformSettings(id, platformSettings);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
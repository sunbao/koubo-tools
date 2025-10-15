package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.User;
import com.koubotools.scriptgenerator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @CacheEvict(value = "users", key = "#username")
    public User createUser(String username, String broadcastType) {
        User user = new User(username, broadcastType);
        return userRepository.save(user);
    }
    
    @Cacheable(value = "users", key = "#id")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * 更新用户账号风格配置
     */
    @CacheEvict(value = "users", allEntries = true)
    public User updateAccountStyle(Long userId, String accountStyle) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAccountStyle(accountStyle);
            return userRepository.save(user);
        }
        return null;
    }
    
    /**
     * 更新用户偏好设置
     */
    @CacheEvict(value = "users", allEntries = true)
    public User updatePreferences(Long userId, String preferences) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPreferences(preferences);
            return userRepository.save(user);
        }
        return null;
    }
    
    /**
     * 更新平台适配配置
     */
    @CacheEvict(value = "users", allEntries = true)
    public User updatePlatformSettings(Long userId, String platformSettings) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPlatformSettings(platformSettings);
            return userRepository.save(user);
        }
        return null;
    }
}
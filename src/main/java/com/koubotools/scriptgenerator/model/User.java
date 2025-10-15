package com.koubotools.scriptgenerator.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    private String username;
    
    private String broadcastType;
    
    @Column(columnDefinition = "TEXT")
    private String accountStyle;
    
    @Column(columnDefinition = "TEXT")
    private String preferences;
    
    @Column(columnDefinition = "TEXT")
    private String platformSettings;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public User() {}
    
    public User(String username, String broadcastType) {
        this.username = username;
        this.broadcastType = broadcastType;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getBroadcastType() {
        return broadcastType;
    }
    
    public void setBroadcastType(String broadcastType) {
        this.broadcastType = broadcastType;
    }
    
    public String getAccountStyle() {
        return accountStyle;
    }
    
    public void setAccountStyle(String accountStyle) {
        this.accountStyle = accountStyle;
    }
    
    public String getPreferences() {
        return preferences;
    }
    
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    
    public String getPlatformSettings() {
        return platformSettings;
    }
    
    public void setPlatformSettings(String platformSettings) {
        this.platformSettings = platformSettings;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
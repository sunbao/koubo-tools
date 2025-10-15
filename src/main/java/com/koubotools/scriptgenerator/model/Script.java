package com.koubotools.scriptgenerator.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scripts")
public class Script {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scriptId;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(columnDefinition = "TEXT")
    private String style;
    
    private String platform;
    
    private Long hotReference;
    
    private String status;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public Script() {}
    
    public Script(String title, String content, String platform) {
        this.title = title;
        this.content = content;
        this.platform = platform;
        this.status = "draft";
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getScriptId() {
        return scriptId;
    }
    
    public void setScriptId(Long scriptId) {
        this.scriptId = scriptId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getStyle() {
        return style;
    }
    
    public void setStyle(String style) {
        this.style = style;
    }
    
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public Long getHotReference() {
        return hotReference;
    }
    
    public void setHotReference(Long hotReference) {
        this.hotReference = hotReference;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
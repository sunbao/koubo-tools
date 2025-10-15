package com.koubotools.scriptgenerator.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hotspots")
public class Hotspot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotId;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String platform;
    
    private Double heatIndex;
    
    @ElementCollection
    private List<String> tags;
    
    @Column(columnDefinition = "TEXT")
    private String vector;
    
    private String type;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public Hotspot() {}
    
    public Hotspot(String title, String content, String platform, Double heatIndex) {
        this.title = title;
        this.content = content;
        this.platform = platform;
        this.heatIndex = heatIndex;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getHotId() {
        return hotId;
    }
    
    public void setHotId(Long hotId) {
        this.hotId = hotId;
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
    
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public Double getHeatIndex() {
        return heatIndex;
    }
    
    public void setHeatIndex(Double heatIndex) {
        this.heatIndex = heatIndex;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public String getVector() {
        return vector;
    }
    
    public void setVector(String vector) {
        this.vector = vector;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
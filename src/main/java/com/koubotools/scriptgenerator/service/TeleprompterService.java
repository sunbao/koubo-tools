package com.koubotools.scriptgenerator.service;

import com.koubotools.scriptgenerator.model.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TeleprompterService {
    
    @Autowired
    private TeleprompterWebSocketHandler webSocketHandler;
    
    /**
     * 将文案推送到提词器
     */
    public boolean pushToTeleprompter(Script script) {
        try {
            // 通过WebSocket将文案推送到所有连接的客户端
            String message = "SCRIPT:" + script.getContent();
            webSocketHandler.broadcastMessageToAll(message);
            
            // 记录日志
            System.out.println("文案已推送到提词器: " + script.getTitle());
            return true;
        } catch (IOException e) {
            System.err.println("推送文案到提词器失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取提词器同步状态
     */
    public String getTeleprompterStatus() {
        // 实现获取提词器状态的逻辑
        // 返回设备连接状态、同步状态等信息
        
        // 模拟返回状态
        return "connected";
    }
    
    /**
     * 控制提词器播放/暂停等操作
     */
    public boolean controlTeleprompter(String command) {
        try {
            // 通过WebSocket发送控制命令
            String message = "CONTROL:" + command;
            webSocketHandler.broadcastMessageToAll(message);
            
            // 记录日志
            System.out.println("提词器控制命令已执行: " + command);
            return true;
        } catch (IOException e) {
            System.err.println("发送提词器控制命令失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据内容智能提醒语速
     */
    public int calculateOptimalSpeed(String content) {
        // 根据内容长度和复杂度计算最佳语速
        // 返回每分钟建议的字数
        
        if (content == null || content.isEmpty()) {
            return 200; // 默认语速
        }
        
        int length = content.length();
        
        // 基于长度和复杂度的智能计算
        if (length < 100) {
            // 短内容可以稍快
            return Math.max(220, 300 - length); 
        } else if (length < 300) {
            // 中等长度，正常语速
            return 200;
        } else if (length < 500) {
            // 较长内容，稍慢一些
            return 180;
        } else {
            // 很长的内容，慢一些确保清晰度
            return Math.max(150, 200 - (length / 50));
        }
    }
    
    /**
     * 计算滚动持续时间(毫秒)
     */
    public double calculateScrollDuration(String content, int speed) {
        if (content == null || content.isEmpty() || speed <= 0) {
            return 1.0;
        }
        
        // 估算行数 (假设每行30个字符)
        int lines = (int) Math.ceil((double) content.length() / 30);
        
        // 每行显示时间(秒) = 60 / (speed / 30)
        // speed是每分钟字数，假设每行30字
        double timePerLine = 60.0 / (speed / 30.0);
        
        // 总时间
        double totalTime = lines * timePerLine;
        
        return totalTime;
    }
    
    /**
     * 向特定用户发送消息
     */
    public boolean sendMessageToUser(Long userId, String message) {
        try {
            webSocketHandler.sendMessageToUser(userId, message);
            return true;
        } catch (IOException e) {
            System.err.println("发送消息给用户失败: " + e.getMessage());
            return false;
        }
    }
}
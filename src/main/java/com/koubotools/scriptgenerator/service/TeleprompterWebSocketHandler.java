package com.koubotools.scriptgenerator.service;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TeleprompterWebSocketHandler extends TextWebSocketHandler {
    
    // 存储所有活跃的WebSocket会话
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    // 存储会话与用户ID的映射
    private final Map<String, Long> sessionUserMap = new ConcurrentHashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 连接建立时，将会话添加到映射中
        sessions.put(session.getId(), session);
        System.out.println("提词器WebSocket连接已建立: " + session.getId());
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理从客户端收到的消息
        String payload = message.getPayload();
        System.out.println("收到消息: " + payload);
        
        // 解析消息并处理
        handleMessage(session, payload);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭时，从映射中移除会话
        sessions.remove(session.getId());
        sessionUserMap.remove(session.getId());
        System.out.println("提词器WebSocket连接已关闭: " + session.getId());
    }
    
    /**
     * 处理收到的消息
     */
    private void handleMessage(WebSocketSession session, String message) throws IOException {
        // 简单的消息处理逻辑
        // 实际项目中可能需要解析JSON格式的消息
        
        if (message.startsWith("REGISTER:")) {
            // 注册用户ID
            String userIdStr = message.substring(9);
            try {
                Long userId = Long.parseLong(userIdStr);
                sessionUserMap.put(session.getId(), userId);
                session.sendMessage(new TextMessage("REGISTERED:" + userId));
            } catch (NumberFormatException e) {
                session.sendMessage(new TextMessage("ERROR:Invalid user ID"));
            }
        } else if (message.startsWith("SYNC:")) {
            // 同步消息到所有连接的客户端
            String syncData = message.substring(5);
            broadcastMessage("SYNC:" + syncData, session.getId());
        } else if (message.startsWith("CONTROL:")) {
            // 控制命令
            String command = message.substring(8);
            broadcastMessage("CONTROL:" + command, session.getId());
        }
    }
    
    /**
     * 广播消息给所有连接的客户端（除了发送者）
     */
    private void broadcastMessage(String message, String senderSessionId) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            String sessionId = entry.getKey();
            WebSocketSession session = entry.getValue();
            
            // 不发送给发送者自己
            if (!sessionId.equals(senderSessionId) && session.isOpen()) {
                session.sendMessage(textMessage);
            }
        }
    }
    
    /**
     * 向特定用户发送消息
     */
    public void sendMessageToUser(Long userId, String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        
        for (Map.Entry<String, Long> entry : sessionUserMap.entrySet()) {
            String sessionId = entry.getKey();
            Long sessionUserId = entry.getValue();
            
            if (sessionUserId.equals(userId)) {
                WebSocketSession session = sessions.get(sessionId);
                if (session != null && session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            }
        }
    }
    
    /**
     * 向所有用户广播消息
     */
    public void broadcastMessageToAll(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
            }
        }
    }
}
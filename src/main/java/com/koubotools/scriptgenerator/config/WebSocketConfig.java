package com.koubotools.scriptgenerator.config;

import com.koubotools.scriptgenerator.service.TeleprompterWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public TeleprompterWebSocketHandler teleprompterWebSocketHandler() {
        return new TeleprompterWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册提词器WebSocket处理器
        registry.addHandler(teleprompterWebSocketHandler(), "/ws/teleprompter")
                .setAllowedOrigins("*");
    }
}
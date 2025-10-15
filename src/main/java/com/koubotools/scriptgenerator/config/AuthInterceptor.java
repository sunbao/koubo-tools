package com.koubotools.scriptgenerator.config;

import com.koubotools.scriptgenerator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private AuthService authService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        // 排除登录接口和公开接口
        if (requestURI.startsWith("/api/users/login") || 
            requestURI.startsWith("/api/users/register") ||
            requestURI.startsWith("/ws/")) {  // WebSocket连接不需要token验证
            return true;
        }
        
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // 去掉"Bearer "前缀
        }
        
        // 验证token
        if (token != null && authService.validateToken(token)) {
            // 将用户ID放入请求属性中，供后续使用
            Long userId = authService.getUserIdFromToken(token);
            request.setAttribute("userId", userId);
            return true;
        }
        
        // token无效，返回401未授权
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\": \"Unauthorized\"}");
        return false;
    }
}
package com.imense.loneworking.infrastructure.config;

import com.imense.loneworking.infrastructure.security.JwtUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Map;
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public AuthHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String authToken = request.getHeaders().getFirst("Authorization");

        if (authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
            Authentication authentication = jwtUtil.getAuthentication(authToken);
            if (authentication != null) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (jwtUtil.validateToken(authToken, userDetails)) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        // No implementation needed
    }
}

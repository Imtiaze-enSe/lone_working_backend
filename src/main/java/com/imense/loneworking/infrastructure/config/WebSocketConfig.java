package com.imense.loneworking.infrastructure.config;

import com.imense.loneworking.infrastructure.websocket.LocationWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.imense.loneworking.infrastructure.websocket.WebSocketService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final LocationWebSocketHandler locationWebSocketHandler;
    private final WebSocketService webSocketService;

    public WebSocketConfig(LocationWebSocketHandler locationWebSocketHandler, WebSocketService webSocketService) {
        this.locationWebSocketHandler = locationWebSocketHandler;
        this.webSocketService = webSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CustomWebSocketHandler(locationWebSocketHandler, webSocketService), "/ws")
                .setAllowedOrigins("http://localhost:3000"); // Update to match your CORS configuration
    }

    private static class CustomWebSocketHandler extends LocationWebSocketHandler {
        private final WebSocketService webSocketService;

        public CustomWebSocketHandler(LocationWebSocketHandler delegate, WebSocketService webSocketService) {
            super(delegate.getLocationService(), delegate.getObjectMapper());
            this.webSocketService = webSocketService;
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            super.afterConnectionEstablished(session);
            webSocketService.addSession(session);
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            super.afterConnectionClosed(session, status);
            webSocketService.removeSession(session);
        }
    }
}

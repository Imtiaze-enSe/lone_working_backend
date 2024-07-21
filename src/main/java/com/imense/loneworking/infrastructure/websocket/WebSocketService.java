package com.imense.loneworking.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class WebSocketService {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper;

    public WebSocketService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void sendLocationUpdate(LocationUpdateDto locationUpdate) {
        sessions.forEach(session -> {
            try {
                String message = objectMapper.writeValueAsString(locationUpdate);
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
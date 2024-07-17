package com.imense.loneworking.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.LocationService;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Getter
@Component
public class LocationWebSocketHandler extends TextWebSocketHandler {

    // Getter methods for use in WebSocketConfig
    private final LocationService locationService;
    private final ObjectMapper objectMapper;

    public LocationWebSocketHandler(LocationService locationService, ObjectMapper objectMapper) {
        this.locationService = locationService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);
        try {
            LocationUpdateDto locationUpdate = objectMapper.readValue(payload, LocationUpdateDto.class);
            locationService.processLocationUpdate(locationUpdate);
        } catch (Exception e) {
            // Log the error and potentially send an error message back to the client
            System.err.println("Error processing location update: " + e.getMessage());
            session.sendMessage(new TextMessage("Error processing location update"));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established. Session ID: " + session.getId());
        // You can also log client information
        System.out.println("Client info: " + session.getRemoteAddress());
        // You can add any logic here that needs to run when a new WebSocket connection is established
        System.out.println("New WebSocket connection established");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // You can add any cleanup logic here that needs to run when a WebSocket connection is closed
        System.out.println("WebSocket connection closed");
    }

}
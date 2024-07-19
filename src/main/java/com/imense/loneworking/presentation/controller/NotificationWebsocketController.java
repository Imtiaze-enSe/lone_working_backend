package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Notification.NotificationCreationDto;
import com.imense.loneworking.application.service.serviceInterface.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationWebsocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationWebsocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    // mapped as app/send
    @MessageMapping("/send")
    @SendTo("/all/notifications")
    public String sendNotification(String message) {
        System.out.println(message);
        return message;
    }
    // mapped as app/private
    @MessageMapping("/private")
    public NotificationCreationDto sendToSpeceficUser(@Payload NotificationCreationDto notification) {
        simpMessagingTemplate.convertAndSendToUser(notification.getSent_to(), "/specific", notification);
        return notification;
    }
}
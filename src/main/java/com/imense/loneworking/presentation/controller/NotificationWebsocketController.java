package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Notification.NotificationCreationDto;
import com.imense.loneworking.application.service.serviceInterface.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationWebsocketController {
    private final NotificationService notificationService;

    public NotificationWebsocketController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @MessageMapping("/send")
    public void sendNotification(@Payload NotificationCreationDto notification) {
        notificationService.sendNotification(notification);
    }
}

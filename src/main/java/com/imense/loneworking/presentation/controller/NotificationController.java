package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Notification.NotificationCreationDto;
import com.imense.loneworking.application.dto.Notification.NotificationInfoDto;
import com.imense.loneworking.application.service.serviceInterface.NotificationService;
import com.imense.loneworking.domain.entity.Notification;
import org.aspectj.bridge.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("web/notifications")
    public List<NotificationInfoDto> getAllNotifications(){
        return notificationService.getAllNotifications();
    }
    @DeleteMapping("web/notification/{id}")
    public void deleteNotification(@PathVariable Long id){
        notificationService.deleteNotification(id);
    }

}

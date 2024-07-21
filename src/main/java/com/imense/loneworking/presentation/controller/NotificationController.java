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
    private final SimpMessagingTemplate simpMessagingTemplate;

    public NotificationController(NotificationService notificationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.notificationService = notificationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

//    @PostMapping("web/notification")
//    public Notification addNotification(@RequestBody NotificationCreationDto notificationCreationDto){
//        return  notificationService.addNotification(notificationCreationDto);
//    }

    @GetMapping("web/notifications")
    public List<NotificationInfoDto> getAllNotifications(){
        return notificationService.getAllNotifications();
    }
    @DeleteMapping("web/notification/{id}")
    public void deleteNotification(@PathVariable Long id){
        notificationService.deleteNotification(id);
    }

//    // mapped as application/send
//    @MessageMapping("/send")
//    @SendTo("/all/notifications")
//    public String sendNotification(String message) {
//        System.out.println(message);
//        return message;
//    }
//    // mapped as application/private
//    @MessageMapping("/private")
//    public NotificationCreationDto sendToSpeceficUser(@Payload NotificationCreationDto notification) {
//        simpMessagingTemplate.convertAndSendToUser(notification.getSent_to(), "/specific", notification);
//        return notification;
//    }
}

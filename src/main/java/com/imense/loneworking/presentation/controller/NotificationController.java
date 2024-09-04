package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Notification.NotificationInfoDto;
import com.imense.loneworking.application.service.serviceInterface.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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


    @PostMapping("web/notifications/nearby-workers/{id}")
    public ResponseEntity<Void> notifyNearbyWorkers(@PathVariable Long id, @RequestParam Long id_alert) {
        try {
            notificationService.nearbyWorkers(id, id_alert);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

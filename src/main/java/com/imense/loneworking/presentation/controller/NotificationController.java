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

    // Get all notifications
    @GetMapping("web/notifications")
    public ResponseEntity<List<NotificationInfoDto>> getAllNotifications(){
        try {
            List<NotificationInfoDto> notifications = notificationService.getAllNotifications();
            return ResponseEntity.ok(notifications);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Delete a notification
    @DeleteMapping("web/notification/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id){
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Notify nearby workers
    @PostMapping("web/notifications/nearby-workers/{id}")
    public ResponseEntity<Void> notifyNearbyWorkers(@PathVariable Long id, @RequestParam Long id_alert) {
        try {
            notificationService.nearbyWorkers(id, id_alert);
            return ResponseEntity.status(HttpStatus.OK).build();  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

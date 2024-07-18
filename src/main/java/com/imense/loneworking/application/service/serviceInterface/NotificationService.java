package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Notification.NotificationCreationDto;
import com.imense.loneworking.application.dto.Notification.NotificationInfoDto;
import com.imense.loneworking.domain.entity.Notification;

import java.util.List;


public interface NotificationService {
    Notification addNotification(NotificationCreationDto notificationCreationDto);

    List<NotificationInfoDto> getAllNotifications();

    void deleteNotification(Long notificationId);

    void sendNotification(String message);
}

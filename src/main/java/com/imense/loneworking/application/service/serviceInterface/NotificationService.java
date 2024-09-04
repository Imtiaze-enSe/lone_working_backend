package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Notification.NotificationCreationDto;
import com.imense.loneworking.application.dto.Notification.NotificationInfoDto;
import com.imense.loneworking.application.dto.Worker.NearbyWorkersDto;
import com.imense.loneworking.domain.entity.Notification;

import java.util.List;


public interface NotificationService {

    List<NotificationInfoDto> getAllNotifications();

    void deleteNotification(Long notificationId);

    void sendNotification(NotificationCreationDto notificationCreationDto);

    void nearbyWorkers(Long id, Long id_alert);
}

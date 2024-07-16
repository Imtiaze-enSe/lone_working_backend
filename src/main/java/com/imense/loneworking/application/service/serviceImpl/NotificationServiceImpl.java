package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Notification.NotificationCreationDto;
import com.imense.loneworking.application.dto.Notification.NotificationInfoDto;
import com.imense.loneworking.application.service.serviceInterface.NotificationService;
import com.imense.loneworking.domain.entity.Notification;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.NotificationRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;



    public NotificationServiceImpl(NotificationRepository notificationRepository,SimpMessagingTemplate simpMessagingTemplate,UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate= simpMessagingTemplate;
        this.userRepository =userRepository;
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
    @Override
    public Notification addNotification(NotificationCreationDto notificationCreationDto) {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);

        Notification notification=new Notification();
        notification.setNotification_title(notificationCreationDto.getTitle());
        notification.setNotification_message(notificationCreationDto.getMessage());
        notification.setNotification_sent_to(notificationCreationDto.getSent_to());
        notification.setUser(authUser);

        simpMessagingTemplate.convertAndSend("/topic/notifications", notificationCreationDto.getMessage());
        return notificationRepository.save(notification);
    }


    @Override
    public List<NotificationInfoDto> getAllNotifications() {
        String username = getCurrentUsername();
        User user = userRepository.findByEmail(username);
        List<Notification> notifications=user.getNotifications();
        List<NotificationInfoDto> notificationInfoDtos=new ArrayList<>();

        for (Notification notification:notifications){
            NotificationInfoDto notificationInfoDto=new NotificationInfoDto();
            notificationInfoDto.setId(notification.getId_notification());
            notificationInfoDto.setTitle(notification.getNotification_title());
            notificationInfoDto.setMessage(notification.getNotification_message());
            notificationInfoDto.setSent_to(notification.getNotification_sent_to());
            notificationInfoDto.setCreatedAt(notification.getNotification_created_at());
            notificationInfoDtos.add(notificationInfoDto);

        }

        return notificationInfoDtos;
    }

    @Override
    public void deleteNotification(Long notificationId) {
        Notification notification=notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
        notificationRepository.delete(notification);

    }
}

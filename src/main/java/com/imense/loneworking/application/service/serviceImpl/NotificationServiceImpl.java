package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Notification.NotificationBroadcastDto;
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
    public void sendNotification(NotificationCreationDto notificationCreationDto) {
        System.out.println("Sending notification: " + notificationCreationDto);
        User authUser = userRepository.findByEmail(notificationCreationDto.getSent_by());

        // Save the notification to the database
        Notification notification = new Notification();
        notification.setNotification_title(notificationCreationDto.getTitle());
        notification.setNotification_message(notificationCreationDto.getMessage());
        notification.setNotification_sent_to(notificationCreationDto.getSent_to());
        notification.setUser(authUser);

        // Save the notification and get the ID
        Notification savedNotification = notificationRepository.save(notification);

        // Prepare notification data including ID
        NotificationBroadcastDto notificationWithId = new NotificationBroadcastDto();
        notificationWithId.setId(savedNotification.getId_notification());
        notificationWithId.setTitle(notificationCreationDto.getTitle());
        notificationWithId.setMessage(notificationCreationDto.getMessage());
        notificationWithId.setSent_by(notificationCreationDto.getSent_by());
        notificationWithId.setSent_to(notificationCreationDto.getSent_to());
        notificationWithId.setSite_id(notificationCreationDto.getSite_id());


        if ("all".equalsIgnoreCase(notificationCreationDto.getSent_to())) {
            System.out.println("Sending to all users with site_id: " + notificationCreationDto.getSite_id());
            // Send to all users with the specified site_id
            simpMessagingTemplate.convertAndSend(
                    "/topic/notifications/site/" + notificationCreationDto.getSite_id(),
                    notificationWithId
            );
        } else {
            // Send to a specific user, but only if they belong to the specified site
            User targetUser = userRepository.findByEmail(notificationCreationDto.getSent_to());
            if (targetUser != null) {
                System.out.println("Sending to specific user: " + targetUser.getEmail());
                simpMessagingTemplate.convertAndSend(
                        "/topic/notifications/site/" + notificationCreationDto.getSite_id() + "/" + targetUser.getId(),
                        notificationWithId
                );
            }
        }
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

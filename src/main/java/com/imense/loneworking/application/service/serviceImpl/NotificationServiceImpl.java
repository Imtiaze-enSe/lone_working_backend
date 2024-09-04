package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Notification.NotificationBroadcastDto;
import com.imense.loneworking.application.dto.Notification.NotificationCreationDto;
import com.imense.loneworking.application.dto.Notification.NotificationInfoDto;
import com.imense.loneworking.application.dto.Worker.NearbyWorkersDto;
import com.imense.loneworking.application.service.serviceInterface.NotificationService;
import com.imense.loneworking.domain.entity.Notification;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.NotificationRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import io.micrometer.common.lang.Nullable;
import org.locationtech.jts.geom.Geometry;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        NotificationBroadcastDto notificationWithId = getNotificationBroadcastDto(notificationCreationDto, savedNotification, null);


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

    @Override
    public void nearbyWorkers(Long id, Long id_alert) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Geometry userLocation = user.getPosition();

            // Get workers from the same site with status "Connected"
            List<User> siteWorkers = userRepository.findBySiteIdAndStatus(user.getSiteId(), "Connected");

            // Filter out the user themselves from the list
            List<User> filteredWorkers = new ArrayList<>(siteWorkers.stream()
                    .filter(worker -> !worker.getId().equals(user.getId()))
                    .toList());

            // Sort workers by distance from the current user
            filteredWorkers.sort(Comparator.comparingDouble(worker -> worker.getPosition().distance(userLocation)));

            // Limit to the 5 nearest workers
            List<User> nearestWorkers = filteredWorkers.stream().limit(5).toList();


            NotificationCreationDto notification = new NotificationCreationDto();
            notification.setTitle(user.getFirst_name() + " " + user.getLast_name() + " is asking for help!");
            notification.setMessage("Can you reach his location to check if he is ok?");
            notification.setSent_by(getCurrentUsername());
            notification.setSite_id(user.getSiteId());
            // Send notifications to the 5 nearest workers
            for (User worker : nearestWorkers) {
                System.out.println(worker.toString());
                notification.setSent_to(worker.getEmail());
                sendNotificationToUser(notification, worker.getId(), id_alert);
            }
        }
    }
    // method to send notifications to a specific user
    private void sendNotificationToUser(NotificationCreationDto notification, Long workerId, Long id_alert) {
        Notification SavedNotification = new Notification();
        SavedNotification.setNotification_title(notification.getTitle());
        SavedNotification.setNotification_message(notification.getMessage());
        SavedNotification.setNotification_sent_to(notification.getSent_to());
        SavedNotification.setUser(userRepository.findByEmail(getCurrentUsername()));

        // Save the notification and get the ID
        Notification savedNotification = notificationRepository.save(SavedNotification);

        // Prepare notification data
        NotificationBroadcastDto notificationBroadcast = getNotificationBroadcastDto(notification, savedNotification, id_alert);

        // Send to the specific user
        simpMessagingTemplate.convertAndSend(
                "/topic/notifications/site/" + notification.getSite_id() + "/" + workerId,
                notificationBroadcast
        );
    }

    private static NotificationBroadcastDto getNotificationBroadcastDto(NotificationCreationDto notification, Notification savedNotification, @Nullable Long id_alert) {
        NotificationBroadcastDto notificationBroadcast = new NotificationBroadcastDto();
        notificationBroadcast.setId(savedNotification.getId_notification());
        notificationBroadcast.setTitle(notification.getTitle());
        notificationBroadcast.setMessage(notification.getMessage());
        notificationBroadcast.setSent_by(notification.getSent_by());
        notificationBroadcast.setSent_to(notification.getSent_to());
        notificationBroadcast.setSite_id(notification.getSite_id());
        if (id_alert != null){
            notificationBroadcast.setAlert_id(id_alert);
        }
        return notificationBroadcast;
    }

}

package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Alert.AlertCreationDto;
import com.imense.loneworking.application.dto.Alert.AlertTableDto;
import com.imense.loneworking.application.service.serviceInterface.AlertService;
import com.imense.loneworking.domain.entity.Alert;
import com.imense.loneworking.domain.entity.Enum.UserRole;
import com.imense.loneworking.domain.repository.AlertRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import com.imense.loneworking.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertServiceImpl implements AlertService {
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public AlertServiceImpl(AlertRepository alertRepository,UserRepository userRepository,SimpMessagingTemplate simpMessagingTemplate) {
        this.alertRepository = alertRepository;
        this.userRepository= userRepository;
        this.simpMessagingTemplate =simpMessagingTemplate;
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public void sendAlert(AlertCreationDto alertCreationDto) {
        System.out.println("Sending alert: " + alertCreationDto);
        User authUser=userRepository.findByEmail(alertCreationDto.getAlert_created_by());
        Alert alert=new Alert();

        alert.setUser(authUser);

        alert.setAlert_type(alertCreationDto.getAlert_type());
        alert.setAlert_status(alertCreationDto.getAlert_status());
        alert.setDuration(alertCreationDto.getDuration());
        Alert savedAlert =alertRepository.save(alert);

        AlertTableDto alertTableDto=new AlertTableDto();
        alertTableDto.setId(savedAlert.getId_alert());
        alertTableDto.setType(savedAlert.getAlert_type());
        alertTableDto.setStatus(savedAlert.getAlert_status());
        alertTableDto.setCreatedAt(savedAlert.getAlert_created_at());
        alertTableDto.setCreatedBy(savedAlert.getUser().getFirst_name()+" "+savedAlert.getUser().getLast_name());
            simpMessagingTemplate.convertAndSend(
                    "/topic/alerts/site/" + authUser.getSiteId(),
                    alertTableDto
            );


    }


    @Override
    public List<AlertTableDto> getAlertForTable() {
        String username = getCurrentUsername();
        User user = userRepository.findByEmail(username);
        Long siteId = user.getSiteId();
        List<Alert> alerts = alertRepository.findByUserSiteId(siteId);

        // Convert alerts to AlertTableDto
        List<AlertTableDto> alertTableDtos = alerts.stream().map(alert -> {
            AlertTableDto dto = new AlertTableDto();
            dto.setId(alert.getId_alert());
            dto.setType(alert.getAlert_type());
            dto.setStatus(alert.getAlert_status());
            dto.setCreatedBy(alert.getUser().getFirst_name()+" "+alert.getUser().getLast_name());
            dto.setCreatedAt(alert.getAlert_created_at());
            return dto;
        }).collect(Collectors.toList());

        return alertTableDtos;


    }
}

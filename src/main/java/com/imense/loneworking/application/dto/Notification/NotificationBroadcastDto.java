package com.imense.loneworking.application.dto.Notification;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NotificationBroadcastDto {
    private Long id; // Notification ID
    private String title;
    private String message;
    private String sent_by;
    private String sent_to;
    private Long site_id;
}

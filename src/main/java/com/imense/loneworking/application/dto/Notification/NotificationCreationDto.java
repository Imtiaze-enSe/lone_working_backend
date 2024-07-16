package com.imense.loneworking.application.dto.Notification;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NotificationCreationDto {
    private String title;
    private String message;
    private String sent_to;
}

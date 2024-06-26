package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_notification;
    private String notification_title;
    private String notification_message;
    private String notification_sent_to;
    private Date notification_created_at;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

}

package com.imense.loneworking.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime notification_created_at;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @PrePersist
    protected void onCreate() {
        notification_created_at = LocalDateTime.now();
    }

}

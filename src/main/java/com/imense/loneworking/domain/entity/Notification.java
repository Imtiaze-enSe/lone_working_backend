package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idNotification;
    private String notificationTitle;
    private String notificationMessage;
    private String notificationSentTo;
    private Date notificationCreatedAt;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

}

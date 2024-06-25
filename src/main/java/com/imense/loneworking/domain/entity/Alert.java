package com.imense.loneworking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idAlert;
    private String alertStatus;
    private String alertType;
    private Date alertCreatedAt;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "idWorker")
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "idUpdate")
    private Update update;

}

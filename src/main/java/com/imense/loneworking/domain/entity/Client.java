package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idClient;
    private String emailClient;
    private String passwordClient;
    private String logoClient;
    private String raisonSocial;
    private String adresseDuSiege;

    @OneToMany(mappedBy = "client")
    private Set<Site> sites;

    @OneToMany(mappedBy = "client")
    private Set<Notification> notifications;

}

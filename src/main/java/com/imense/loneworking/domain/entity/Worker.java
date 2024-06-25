package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;
import java.util.UUID;
import java.util.Set;


@Entity
@Getter
@Setter
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idWorker;
    private String firstName;
    private String lastName;
    private String email;
    private String function;
    private String phoneNumber;
    private String photo;
    private String address;
    private String companyName;
    private String companyLogo;
    private String departement;
    private String reportTo;
    private Date workerCreatedAt;
    private String contactPersName;
    private String contactPersPhone;
    private String lienFam;
    private String bloodType;
    private boolean smoking;
    private Geometry workerPosition;

    @ManyToOne
    @JoinColumn(name = "idSite")
    private Site site;

    @OneToMany(mappedBy = "worker")
    private Set<Alert> alerts;

}

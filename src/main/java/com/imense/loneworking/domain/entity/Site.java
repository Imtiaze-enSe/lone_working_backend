package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idSite;
    private String siteName;
    private String siteAddress;
    private Date siteCreatedAt;
    private Geometry siteGeom2D;
    private Geometry siteGeom3D;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @OneToMany(mappedBy = "site")
    private Set<Zone> zones;

    @OneToMany(mappedBy = "site")
    private Set<Worker> workers;

    // Getters and Setters
}

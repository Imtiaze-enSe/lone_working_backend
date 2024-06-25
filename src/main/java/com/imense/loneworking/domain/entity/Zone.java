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
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idZone;
    private String zoneName;
    private Date zoneCreatedAt;
    private Geometry zoneGeom2D;
    private Geometry zoneGeom3D;

    @ManyToOne
    @JoinColumn(name = "idSite")
    private Site site;

    @OneToMany(mappedBy = "zone")
    private Set<Level> levels;

    @OneToMany(mappedBy = "zone")
    private Set<Local> locals;

}

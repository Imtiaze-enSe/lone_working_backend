package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "zones")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private String name;
    private Boolean status;


    private Date created_at;
    private Date updated_at;
    private Date deleted_at;

    private Geometry plan;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @OneToMany(mappedBy = "zone")
    private List<LevelZone> levelZones;

    @OneToMany(mappedBy = "zone")
    private List<LevelRoom> levelRooms;

}


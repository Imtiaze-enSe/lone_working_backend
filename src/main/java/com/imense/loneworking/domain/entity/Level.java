package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "levels")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String sub;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;

    @OneToMany(mappedBy = "level")
    private List<LevelZone> levelZones;

    @OneToMany(mappedBy = "level")
    private List<LevelRoom> levelRooms;

}

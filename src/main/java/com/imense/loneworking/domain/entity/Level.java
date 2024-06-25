package com.imense.loneworking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idLevel;
    private Integer level;

    @ManyToOne
    @JoinColumn(name = "idZone")
    private Zone zone;

    @OneToMany(mappedBy = "level")
    private Set<Local> locals;
}

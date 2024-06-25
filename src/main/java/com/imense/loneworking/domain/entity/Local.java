package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idLocal;
    private String local;

    @ManyToOne
    @JoinColumn(name = "idZone")
    private Zone zone;

    @ManyToOne
    @JoinColumn(name = "idLevel")
    private Level level;

    @OneToMany(mappedBy = "local")
    private Set<Room> rooms;

}

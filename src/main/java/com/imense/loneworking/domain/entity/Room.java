package com.imense.loneworking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idRoom;
    private String room;

    @ManyToOne
    @JoinColumn(name = "idLocal")
    private Local local;

    @OneToMany(mappedBy = "room")
    private Set<QrCode> qrCodes;

}

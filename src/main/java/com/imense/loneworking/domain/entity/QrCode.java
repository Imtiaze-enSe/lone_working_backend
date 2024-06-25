package com.imense.loneworking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idQrCode;
    private Geometry qrCodePosition;
    private Date qrCodeCreatedAt;

    @ManyToOne
    @JoinColumn(name = "idRoom")
    private Room room;

}

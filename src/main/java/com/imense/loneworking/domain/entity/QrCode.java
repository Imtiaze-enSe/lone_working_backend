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
@Table(name = "qrcodes")
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_qr_code;
    private Geometry qr_code_position;
    private Date qr_code_createdAt;

    @ManyToOne
    @JoinColumn(name = "idRoom")
    private Room room;

}

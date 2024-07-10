package com.imense.loneworking.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;
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
    private String level;
    private String room;
    private String interior;
    private LocalDateTime qr_code_createdAt;
    private LocalDateTime qr_code_upatedAt;
    private LocalDateTime qr_code_deletedAt;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @PrePersist
    protected void onCreate() {
        qr_code_createdAt = LocalDateTime.now();
        qr_code_upatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        qr_code_upatedAt = LocalDateTime.now();
    }

    @PreRemove
    protected void onDelete() {
        qr_code_deletedAt = LocalDateTime.now();
    }

}

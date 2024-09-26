package com.imense.loneworking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Entity
@Getter
@Setter
@Table(name = "users_synchro")
public class UserSynchro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_id")  // Map the database column ref_id to this field
    private Long refId;

    private String contact_person;
    private String contact_person_phone;

    private Geometry position;
    private String connectionStatus;

    // health info
    private String drugs;
    private String blood_type;
    private String diseases;
    private String medications;
    private Boolean alcoholic;
    private Boolean smoking;
}

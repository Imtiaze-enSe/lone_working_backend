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
@Table(name = "sites")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String acronym;
    private String tva;
    private String nace;
    @Lob
    private String description;
    @Lob
    private String address;
    private String n_address;
    private String box;
    private String zip_code;
    private String city;
    private String country;
    private String phone;
    private String website;
    private Boolean status;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private String location;

    private Geometry plan2d;
    private Geometry plan3d;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    @OneToMany(mappedBy = "site")
    private List<Zone> zones;

}

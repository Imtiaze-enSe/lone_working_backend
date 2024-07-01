package com.imense.loneworking.domain.entity;

import com.imense.loneworking.domain.entity.Enum.TenantsPresentationTypeEnum;
import com.imense.loneworking.domain.entity.Enum.TenantsTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter

@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private TenantsTypeEnum type;
    private Long parent_id;
    private Boolean status;
    @Enumerated(EnumType.STRING)
    private TenantsPresentationTypeEnum presentation_type;
    private Long contractor_site_id;
    private String email;
    private String acronym;
    private String legal_form;
    private String tva;
    private String nace;
    @Lob
    private String description;
    @Lob
    private String address;
    private String n_address;
    private String box;
    private String zipCode;
    private String city;
    private String country;
    private String phone;
    private String logo;
    private String website;
    private String medicName;
    private String medic_speciality;
    private String medic_phone;
    private String medic_photo;
    private String n_emergency;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;

    @OneToMany(mappedBy = "tenant")
    private List<Site> sites;

    @OneToMany(mappedBy = "tenant")
    private List<User> users;

}

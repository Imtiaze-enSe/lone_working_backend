package com.imense.loneworking.application.dto.Tenant;

import com.imense.loneworking.domain.entity.Enum.TenantsPresentationTypeEnum;
import com.imense.loneworking.domain.entity.Enum.TenantsTypeEnum;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TenantCreationDto {

    @NotNull(message = "Name is required")
    private String name;

    private TenantsTypeEnum type;
    private Long parent_id;
    private Boolean status;
    private TenantsPresentationTypeEnum presentation_type;
    private Long contractor_site_id;
    private String email;
    private String acronym;
    private String legal_form;
    private String tva;
    private String nace;
    private String description;
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
}

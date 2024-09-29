package com.imense.loneworking.application.dto.Worker;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EditProfileMobileDto {
    private Long id;
    private String profile_photo;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;

    private Long function_id;
    private String address;

    private String contact_person;
    private String contact_person_phone;

    private String report_to_first_name;
    private String report_to_last_name;
    private String company_name;
    private Long company_id;

    private String password;

    // health
    private String blood_type;
    private String diseases;
    private String medications;
    private Boolean alcoholic;
    private Boolean smoking;
    private String pin;
    private String drugs;

}

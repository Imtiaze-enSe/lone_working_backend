package com.imense.loneworking.application.dto.Worker;

import jakarta.validation.constraints.Email;
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
    @Email
    private String email;
    private String phone;
    private String function;
    private String address;
    private String password;
    private String contact_person;
    private String contact_person_phone;
    private String report_to;
    private String company_logo;
    private String company_name;
    private String blood_type;
    private String diseases;
    private String medications;
    private Boolean alcoholic;
    private Boolean smoking;
    private String pin;
    private String drugs;

}

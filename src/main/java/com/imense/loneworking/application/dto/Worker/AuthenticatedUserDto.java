package com.imense.loneworking.application.dto.Worker;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AuthenticatedUserDto {
    private Long id;
    private String profile_photo;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String function;
    private String address;
    private String password;
    private Long site_id;
    private String company_logo;
    private String company_name;
    private String company_email;
    private String company_phone;
}

package com.imense.loneworking.application.dto.Worker;


import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WorkerCreationDto {
    private String profile_photo;
    private String first_name;
    private String last_name;
    @Email
    private String email;
    private String password;
    private String phone;
    private String company_logo;
    private String report_to;
    private String department;
    private String function;
    private String site_name;
}

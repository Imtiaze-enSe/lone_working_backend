package com.imense.loneworking.application.dto.Worker;


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
    private String email;
    private String password;
    private String phone;
    private String companyLogo;
    private String linked_to;
    private String department;
    private String function;
}

package com.imense.loneworking.application.dto.Worker;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EditProfileUserDto {
    private Long id;
    private String profile_photo;
    private String first_name;
    private String last_name;
    @Email
    private String email;
    private String phone;
    private String function;
    private String address;
}

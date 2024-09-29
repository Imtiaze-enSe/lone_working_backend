package com.imense.loneworking.application.dto.Worker;

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
    private String email;
    private String phone;
    private Long function_id;
    private String address;
}

package com.imense.loneworking.application.dto;

import com.imense.loneworking.domain.entity.Enum.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class RegistrationDto {
    private String email;
    private String password;
    private UserRole role;

}

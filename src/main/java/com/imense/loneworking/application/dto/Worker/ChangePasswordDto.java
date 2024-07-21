package com.imense.loneworking.application.dto.Worker;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChangePasswordDto {
    private Long id;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}

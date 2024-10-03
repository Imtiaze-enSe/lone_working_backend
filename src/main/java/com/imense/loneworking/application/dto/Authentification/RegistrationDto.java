package com.imense.loneworking.application.dto.Authentification;

import com.imense.loneworking.domain.entity.Enum.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Pattern(regexp = "USER|ADMIN|WORKER", message = "Invalid role")
    private UserRole role;

    private Long site_id;
    private Long tenant_id;
}

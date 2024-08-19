package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.service.serviceInterface.PasswordResetService;
import com.imense.loneworking.infrastructure.security.ResetPasswordJwtToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final ResetPasswordJwtToken resetPasswordJwtToken;

    public PasswordResetController(PasswordResetService passwordResetService, ResetPasswordJwtToken resetPasswordJwtToken) {
        this.passwordResetService = passwordResetService;
        this.resetPasswordJwtToken = resetPasswordJwtToken;
    }

    @PostMapping("reset-password")
    public ResponseEntity<Void> sendResetPasswordLink(@RequestParam String email) {
        passwordResetService.sendResetPasswordLink(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("reset-password/confirm")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean isValid = passwordResetService.validatePasswordResetToken(token, resetPasswordJwtToken.getEmailFromToken(token));
        if (isValid) {
            passwordResetService.updatePassword(token, newPassword);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

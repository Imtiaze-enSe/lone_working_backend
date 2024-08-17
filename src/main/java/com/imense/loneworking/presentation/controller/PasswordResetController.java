package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.service.serviceInterface.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String email) {
        passwordResetService.resetPassword(email);
        return ResponseEntity.ok("A new password has been sent to your email.");
    }
}

package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Authentification.LoginDto;
import com.imense.loneworking.application.dto.Authentification.RegistrationDto;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.application.service.serviceInterface.AuthService;
import com.imense.loneworking.presentation.response.InvalidPinException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("auth/register")
    public User register(@RequestBody RegistrationDto RegistrationDto) {
        return authService.registerUser(RegistrationDto);
    }
    @PostMapping("auth/loginmobile")
    public String loginMobile(@RequestBody LoginDto loginDto) {
        return authService.authenticateUserMobile(loginDto);
    }
    @PostMapping("auth/loginmobile/pin")
    public ResponseEntity<String> loginMobilePin(@RequestBody Map<String, String> payload) {
        String pin = payload.get("pin");
        String response = authService.authenticateUserMobilePin(pin);
        return ResponseEntity.ok(response);
    }

    @PostMapping("auth/loginweb")
    public String loginWeb(@RequestBody LoginDto loginDto) {
        return authService.authenticateUserWeb(loginDto);
    }

}

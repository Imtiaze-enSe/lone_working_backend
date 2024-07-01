package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.LoginDto;
import com.imense.loneworking.application.dto.RegistrationDto;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.application.service.serviceInterface.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(loginDto);
        return authService.authenticateUserMobile(loginDto);
    }
    @PostMapping("auth/loginweb")
    public String loginWeb(@RequestBody LoginDto loginDto) {
        System.out.println(loginDto);
        return authService.authenticateUserWeb(loginDto);
    }
}

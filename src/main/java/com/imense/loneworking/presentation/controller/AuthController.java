package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.LoginDto;
import com.imense.loneworking.application.dto.RegistrationDto;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegistrationDto RegistrationDto) {
        return userService.registerUser(RegistrationDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return userService.authenticateUser(loginDto);
    }
}

package com.imense.loneworking.presentation.controller;


import com.imense.loneworking.application.dto.UserDashboardDto;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("web/dashboard/users")
    public List<UserDashboardDto> getUsersForAuthenticatedUser() {
        return userService.getAllUsers();
    }
}

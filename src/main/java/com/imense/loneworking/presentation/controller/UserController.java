package com.imense.loneworking.presentation.controller;


import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.WorkerInfoDto;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("web/dashboard/usersDashboard/sites_id:{site_id}")
    public List<UserDashboardDto> getUsersForAuthenticatedUser(@PathVariable Long site_id) {
        return userService.getAllUsersForDashboard(site_id);
    }

    @GetMapping("web/workers")
    public List<WorkerInfoDto> getAllUsersForTable() {
        return userService.getAllUsersForTable();
    }

}

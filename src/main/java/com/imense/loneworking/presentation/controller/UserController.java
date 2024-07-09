package com.imense.loneworking.presentation.controller;


import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.WorkerCreationDto;
import com.imense.loneworking.application.dto.Worker.WorkerInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.Zone;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("web/dashboard/usersDashboard/site_id:{site_id}")
    public List<UserDashboardDto> getUsersForAuthenticatedUser(@PathVariable Long site_id) {
        return userService.getAllUsersForDashboard(site_id);
    }

    @GetMapping("web/workers")
    public List<WorkerInfoDto> getAllUsersForTable() {
        return userService.getAllUsersForTable();
    }

    @PostMapping("web/worker")
    public User addZone(@RequestBody WorkerCreationDto workerCreationDto) {
        return userService.addWorker(workerCreationDto);
    }
    @PutMapping("web/worker/{id}")
    public User updateZone(@PathVariable Long id, @RequestBody WorkerCreationDto workerCreationDto) {
        return userService.updateWorker(id, workerCreationDto);
    }
    @DeleteMapping("web/worker/{id}")
    public void deleteWorker(@PathVariable Long id) {
        userService.deleteWorker(id);
    }
}

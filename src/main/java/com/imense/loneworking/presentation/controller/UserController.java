package com.imense.loneworking.presentation.controller;


import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.Zone;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> addWorker(@RequestBody WorkerCreationDto workerCreationDto) {
        User newUser = userService.addWorker(workerCreationDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    @PutMapping("web/worker/{id}")
    public ResponseEntity<User> updateWorker(@PathVariable Long id, @RequestBody WorkerCreationDto workerCreationDto) {
        User updatedUser = userService.updateWorker(id, workerCreationDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser); // Return 200 OK with the updated user object
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if user is not found
        }
    }
    @DeleteMapping("web/worker/{id}")
    public void deleteWorker(@PathVariable Long id) {
        userService.deleteWorker(id);
    }

    @GetMapping("web/worker/authenticated")
    public AuthenticatedUserDto getAuthenticatedUserWeb(){
        return userService.getAuthenticatedUser();
    }
    @GetMapping("mobile/worker/authenticated")
    public AuthenticatedUserDto getAuthenticatedUserMobile(){
        return userService.getAuthenticatedUser();
    }

    @PutMapping("web/worker/authenticated")
    public ResponseEntity<User> editProfileUser(@RequestBody EditProfileUserDto editProfileUserDto){
        User updatedUser = userService.editProfileUser(editProfileUserDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser); // Return 200 OK with the updated user object
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if user is not found
        }
    }


    @PutMapping("web/worker/authenticated/password")
    public ResponseEntity<User> changePasswordUser(@RequestBody ChangePasswordDto changePasswordDto){
        User updatedUser = userService.changePasswordUser(changePasswordDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser); // Return 200 OK with the updated user object
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if user is not found
        }
    }
    @GetMapping("mobile/worker/settings")
    public EditProfileMobileDto getEditProfileUser(){
        return userService.getUserForMobileSettings();
    }
    @PutMapping("mobile/worker/settings")
    public ResponseEntity<User> settingsMobile(@RequestBody EditProfileMobileDto editProfileMobileDto){
        User updatedUser = userService.settingsMobile(editProfileMobileDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser); // Return 200 OK with the updated user object
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if user is not found
        }
    }
}

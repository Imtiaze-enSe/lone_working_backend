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
    public ResponseEntity<List<UserDashboardDto>> getUsersForAuthenticatedUser(@PathVariable Long site_id) {
        List<UserDashboardDto> users = userService.getAllUsersForDashboard(site_id);
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Return 204 No Content if no users are found
        }
    }

    @GetMapping("web/workers")
    public ResponseEntity<List<WorkerInfoDto>> getAllUsersForTable() {
        List<WorkerInfoDto> workers = userService.getAllUsersForTable();
        return workers != null && !workers.isEmpty() ? ResponseEntity.ok(workers) : ResponseEntity.noContent().build();
    }

    @PostMapping("web/worker")
    public ResponseEntity<User> addWorker(@RequestBody WorkerCreationDto workerCreationDto) {
        User newUser = userService.addWorker(workerCreationDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("web/worker/{id}")
    public ResponseEntity<User> updateWorker(@PathVariable Long id, @RequestBody WorkerCreationDto workerCreationDto) {
        User updatedUser = userService.updateWorker(id, workerCreationDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("web/worker/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        userService.deleteWorker(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content when the worker is successfully deleted
    }

    @GetMapping("web/worker/authenticated")
    public ResponseEntity<AuthenticatedUserDto> getAuthenticatedUserWeb() {
        AuthenticatedUserDto user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("mobile/worker/authenticated")
    public ResponseEntity<AuthenticatedUserDto> getAuthenticatedUserMobile() {
        AuthenticatedUserDto user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("web/worker/authenticated")
    public ResponseEntity<User> editProfileUser(@RequestBody EditProfileUserDto editProfileUserDto) {
        User updatedUser = userService.editProfileUser(editProfileUserDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("web/worker/authenticated/password")
    public ResponseEntity<User> changePasswordUser(@RequestBody ChangePasswordDto changePasswordDto) {
        User updatedUser = userService.changePasswordUser(changePasswordDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("mobile/worker/settings")
    public ResponseEntity<EditProfileMobileDto> getEditProfileUser() {
        EditProfileMobileDto profile = userService.getUserForMobileSettings();
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.noContent().build();
    }

    @PutMapping("mobile/worker/settings")
    public ResponseEntity<User> settingsMobile(@RequestBody EditProfileMobileDto editProfileMobileDto) {
        User updatedUser = userService.settingsMobile(editProfileMobileDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("mobile/worker/pinsettings")
    public ResponseEntity<PinSettingsDto> getPinSettingsUser() {
        PinSettingsDto pinSettings = userService.getPinSettings();
        return pinSettings != null ? ResponseEntity.ok(pinSettings) : ResponseEntity.noContent().build();
    }

    @PutMapping("mobile/worker/pinsettings")
    public ResponseEntity<User> pinSettingsMobile(@RequestBody PinSettingsDto pinSettingsDto) {
        User updatedUser = userService.updateUserPin(pinSettingsDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("mobile/worker/terms")
    public ResponseEntity<UserTermsDto> getTermsUser() {
        UserTermsDto terms = userService.getUserTerms();
        return terms != null ? ResponseEntity.ok(terms) : ResponseEntity.noContent().build();
    }

    @PutMapping("mobile/worker/terms")
    public ResponseEntity<User> termsUser(@RequestBody UserTermsDto userTermsDto) {
        User updatedUser = userService.updateUserTerms(userTermsDto);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

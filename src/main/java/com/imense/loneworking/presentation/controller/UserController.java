package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.application.service.serviceInterface.UserServiceSynchro;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.UserSynchro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class UserController {
    private final UserService userService;
    private final UserServiceSynchro userServiceSynchro;

    public UserController(UserService userService, UserServiceSynchro userServiceSynchro) {
        this.userService = userService;
        this.userServiceSynchro = userServiceSynchro;
    }

    @GetMapping("web/dashboard/usersDashboard/tenant:{tenant_id}")
    public ResponseEntity<List<UserDashboardDto>> getUsersForAuthenticatedUser(@PathVariable Long tenant_id) {
        List<UserDashboardDto> users = userServiceSynchro.getAllUsersForDashboard(tenant_id);
        if (users != null && !users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Return 204 No Content if no users are found
        }
    }

    @GetMapping("web/workers/tenant:{tenand_id}")
    public ResponseEntity<List<WorkerInfoDto>> getAllUsersForTable(@PathVariable Long tenand_id) {
        List<WorkerInfoDto> workers = userServiceSynchro.getAllUsersForTable(tenand_id);
        return workers != null && !workers.isEmpty() ? ResponseEntity.ok(workers) : ResponseEntity.noContent().build();
    }

    @PostMapping("/web/worker")
    public ResponseEntity<UserSynchro> addWorker(
            @RequestBody WorkerCreationDto workerCreationDto
    ) {
        try {
            Mono<UserSynchro> newUser = userServiceSynchro.addWorker(workerCreationDto);
            // Wait for the Mono to complete and get the result
            UserSynchro userSynchro = newUser.block();  // Blocking for simplicity

            return ResponseEntity.status(HttpStatus.CREATED).body(userSynchro);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("web/worker/{id}")
    public ResponseEntity<Mono<Map>> updateWorker(
            @PathVariable Long id,
            @RequestBody WorkerCreationDto workerCreationDto
    ) {
        try {
            Mono<Map> updatedUser = userServiceSynchro.updateWorker(id, workerCreationDto);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("web/worker/{id}")
    public ResponseEntity<?> deleteWorker(@PathVariable Long id) {
        try {
            // Delete the worker from the external API
            Mono<Map<String, Object>> response = userServiceSynchro.deleteWorker(id);

            // Check the response and delete from local database if successful
            Map<String, Object> apiResponse = response.block();

            // Check if the deletion was successful in the external API
            if (Boolean.TRUE.equals(apiResponse.get("success"))) {
                // Now delete from the local database
                userServiceSynchro.deletWorkerFromDatabase(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Worker not found in external system.");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete worker");
        }
    }



    @GetMapping("web/worker/authenticated/{id}")
    public ResponseEntity<AuthenticatedUserDto> getAuthenticatedUserWeb(@PathVariable Long id) {
        AuthenticatedUserDto user = userServiceSynchro.getAuthenticatedUser(id);
        return ResponseEntity.ok(user);
    }


    @GetMapping("mobile/worker/authenticated/{id}")
    public ResponseEntity<AuthenticatedUserDto> getAuthenticatedUserMobile(@PathVariable Long id) {
        AuthenticatedUserDto user = userServiceSynchro.getAuthenticatedUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("web/worker/authenticated")
    public ResponseEntity<Mono<Map<String, Object>>> editProfileUser(@RequestBody EditProfileUserDto editProfileUserDto) {
        try {
            // Call the service method to update the profile
            Mono<Map<String, Object>> updatedUserResponse = userServiceSynchro.editProfileUser(editProfileUserDto);

            // Return the Mono response, handled reactively
            return ResponseEntity.ok(updatedUserResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("web/worker/authenticated/password")
    public ResponseEntity<Mono<Map<String, Object>>> changePasswordUser(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            Mono<Map<String, Object>> updatedUserResponse = userServiceSynchro.changePasswordUser(changePasswordDto);
            return ResponseEntity.ok(updatedUserResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("mobile/worker/settings/{id}")
    public ResponseEntity<Mono<EditProfileMobileDto>> getEditProfileUser(@PathVariable Long id) {
        try {
            Mono<EditProfileMobileDto> profile = userServiceSynchro.getUserForMobileSettings(id);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }


    @PutMapping("mobile/worker/settings")
    public ResponseEntity<Mono<Map<String, Object>>> settingsMobile(@RequestBody EditProfileMobileDto editProfileMobileDto) {
        try {
            Mono<Map<String, Object>> updatedUser = userServiceSynchro.settingsMobile(editProfileMobileDto);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("mobile/worker/pinsettings")
    public ResponseEntity<PinSettingsDto> getPinSettingsUser() {
        PinSettingsDto pinSettings = userService.getPinSettings();
        return pinSettings != null ? ResponseEntity.ok(pinSettings) : ResponseEntity.noContent().build();
    }

    @PutMapping("mobile/worker/pinsettings")
    public ResponseEntity<Mono<Void>> updateUserPin(@RequestBody PinSettingsUpdate pinSettingsUpdate) {
        try {
            Mono<Void> updatedUser = userServiceSynchro.updateUserPin(pinSettingsUpdate);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


//    @GetMapping("mobile/worker/terms")
//    public ResponseEntity<UserTermsDto> getTermsUser() {
//        UserTermsDto terms = userService.getUserTerms();
//        return terms != null ? ResponseEntity.ok(terms) : ResponseEntity.noContent().build();
//    }

//    @PutMapping("mobile/worker/terms")
//    public ResponseEntity<User> termsUser(@RequestBody UserTermsDto userTermsDto) {
//        User updatedUser = userService.updateUserTerms(userTermsDto);
//        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }

    @GetMapping("mobile/user/{userId}/inside-site")
    public ResponseEntity<String> isUserInSite(@PathVariable Long userId,
                                               @RequestParam Double longitude,
                                               @RequestParam Double latitude) {
        boolean isInside = userServiceSynchro.isUserInSite(userId, longitude, latitude);

        if (isInside) {
            return ResponseEntity.ok("User is inside the site");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not inside the site");
        }
    }
}

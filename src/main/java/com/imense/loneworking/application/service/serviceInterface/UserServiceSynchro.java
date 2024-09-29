package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.domain.entity.UserSynchro;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface UserServiceSynchro {
    List<UserDashboardDto> getAllUsersForDashboard(Long tenantId);
    List<WorkerInfoDto> getAllUsersForTable(Long tenantId);
    AuthenticatedUserDto getAuthenticatedUser(Long userId);
    Mono<UserSynchro> addWorker(WorkerCreationDto workerCreationDto);
    Mono<Map> updateWorker(Long userId, WorkerCreationDto workerCreationDto);
    Mono<Map<String, Object>> deleteWorker(Long workerId);
    void deletWorkerFromDatabase(Long siteId);
    Mono<Map<String, Object>> editProfileUser(EditProfileUserDto editProfileUserDto);
    Mono<Map<String, Object>> changePasswordUser(ChangePasswordDto changePasswordDto);
    Mono<EditProfileMobileDto> getUserForMobileSettings(Long userId);
    Mono<Map<String, Object>> settingsMobile(EditProfileMobileDto editProfileMobileDto);
    Mono<Void> updateUserPin(PinSettingsUpdate pinSettingsUpdate);

    boolean isUserInSite(Long userId, Double longitude, Double latitude);
}

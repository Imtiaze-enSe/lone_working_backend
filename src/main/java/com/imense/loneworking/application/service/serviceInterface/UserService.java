package com.imense.loneworking.application.service.serviceInterface;


import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.domain.entity.User;

import java.util.List;

public interface UserService {
    List<UserDashboardDto> getAllUsersForDashboard(Long site_id);
    List<WorkerInfoDto> getAllUsersForTable();
    User addWorker(WorkerCreationDto workerCreationDto);
    User updateWorker(Long id, WorkerCreationDto workerCreationDto);
    void deleteWorker(Long workerId);

    AuthenticatedUserDto getAuthenticatedUser();

    User editProfileUser(EditProfileUserDto editProfileUserDto);
    User changePasswordUser(ChangePasswordDto changePasswordDto);
    EditProfileMobileDto getUserForMobileSettings();
    User settingsMobile(EditProfileMobileDto editProfileMobileDto);
}

package com.imense.loneworking.application.service.serviceInterface;


import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.WorkerInfoDto;

import java.util.List;

public interface UserService {
    List<UserDashboardDto> getAllUsersForDashboard();
    List<WorkerInfoDto> getAllUsersForTable();
}

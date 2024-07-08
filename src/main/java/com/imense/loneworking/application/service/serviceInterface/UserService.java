package com.imense.loneworking.application.service.serviceInterface;


import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.WorkerCreationDto;
import com.imense.loneworking.application.dto.Worker.WorkerInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.Zone;

import java.util.List;

public interface UserService {
    List<UserDashboardDto> getAllUsersForDashboard(Long site_id);
    List<WorkerInfoDto> getAllUsersForTable();
    User addZone(WorkerCreationDto workerCreationDto);
}

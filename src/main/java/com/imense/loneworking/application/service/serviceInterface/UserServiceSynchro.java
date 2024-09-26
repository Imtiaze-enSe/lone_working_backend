package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.AuthenticatedUserDto;
import com.imense.loneworking.application.dto.Worker.WorkerInfoDto;
import com.imense.loneworking.domain.repository.SiteSynchroRepository;
import com.imense.loneworking.domain.repository.UserSynchroRepository;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface UserServiceSynchro {
    List<UserDashboardDto> getAllUsersForDashboard(Long tenantId);
    List<WorkerInfoDto> getAllUsersForTable(Long tenantId);
    AuthenticatedUserDto getAuthenticatedUser(Long userId);
}

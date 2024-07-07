package com.imense.loneworking.application.service.serviceInterface;


import com.imense.loneworking.application.dto.UserDashboardDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<UserDashboardDto> getAllUsers();
}

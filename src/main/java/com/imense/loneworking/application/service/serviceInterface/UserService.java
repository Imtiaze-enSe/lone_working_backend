package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.RegistrationDto;
import com.imense.loneworking.application.dto.LoginDto;
import com.imense.loneworking.domain.entity.User;

public interface UserService {
    User registerUser(RegistrationDto registrationDto);
    String authenticateUser(LoginDto loginDto);
}

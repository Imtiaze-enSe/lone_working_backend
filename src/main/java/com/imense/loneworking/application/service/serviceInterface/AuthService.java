package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.RegistrationDto;
import com.imense.loneworking.application.dto.LoginDto;
import com.imense.loneworking.domain.entity.User;

public interface AuthService {
    User registerUser(RegistrationDto registrationDto);
    String authenticateUserWeb(LoginDto loginDto);
    String authenticateUserMobile(LoginDto loginDto);
}

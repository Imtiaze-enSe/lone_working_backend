package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Authentification.RegistrationDto;
import com.imense.loneworking.application.dto.Authentification.LoginDto;
import com.imense.loneworking.domain.entity.User;

public interface AuthService {
    User registerUser(RegistrationDto registrationDto);
    String authenticateUserWeb(LoginDto loginDto);
    String authenticateUserMobile(LoginDto loginDto);
    String authenticateUserMobilePin(String pin);
}

package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Authentification.LoginDto;

public interface SafetyTrackerAuthService {
    String safetyTrackerAuthenticateUserWeb(LoginDto loginDto);
}

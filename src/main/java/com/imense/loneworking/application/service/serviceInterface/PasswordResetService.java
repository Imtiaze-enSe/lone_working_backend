package com.imense.loneworking.application.service.serviceInterface;


public interface PasswordResetService {
    void sendResetPasswordLink(String email);
    boolean validatePasswordResetToken(String token, String email);
    void updatePassword(String token, String newPassword);
}

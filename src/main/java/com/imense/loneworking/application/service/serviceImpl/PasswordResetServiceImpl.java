package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.service.serviceInterface.PasswordResetService;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.infrastructure.security.ResetPasswordJwtToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final JavaMailSender mailSender;
    private final ResetPasswordJwtToken jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.resetPasswordUrl}")
    private String resetPasswordUrl;

    @Value("${app.sentFrom}")
    private String sentFrom;

    public PasswordResetServiceImpl(JavaMailSender mailSender, ResetPasswordJwtToken jwtTokenUtil,
                                    UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }


        // Generate token
        String token = jwtTokenUtil.generateToken(user.getEmail());

        // Create reset link
        String resetLink = resetPasswordUrl + "?token=" + token;

        // Email content
        String emailTitle = "Reset Your Password - Action Required";
        String emailBody = generateEmailBody(user.getFirst_name(), resetLink);

        // Send email
        sendEmail(user.getEmail(), emailTitle, emailBody);
    }

    private String generateEmailBody(String firstName, String resetLink) {
        return "Hello " + firstName + ",\n\n" +
                "We received a request to reset the password associated with your account. " +
                "If you made this request, please click the link below to reset your password:\n\n" +
                resetLink + "\n\n" +
                "**Please note:** This link will expire in 5 minutes. If the link expires, you will need to request a new password reset.\n\n" +
                "If you did not request a password reset, please ignore this email. Your password will remain unchanged.\n\n" +
                "Thank you,\n" +
                "LoneWorking Support Team";
    }

    @Override
    public boolean validatePasswordResetToken(String token, String email) {
        return jwtTokenUtil.validateToken(token, email);
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        String email = jwtTokenUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sentFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}

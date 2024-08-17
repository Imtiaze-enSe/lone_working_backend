package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.service.serviceInterface.PasswordResetService;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetServiceImpl(UserRepository userRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void resetPassword(String email) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // Generate a new temporary password
        String newPassword = UUID.randomUUID().toString().substring(0, 8); // Generate an 8-character password

        // Save the new password (after encoding) in the database
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Send the new password to the user's email
        sendEmail(user.getEmail(), newPassword);
    }

    private void sendEmail(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your New Password");
        message.setText("Your new password is: " + newPassword);
        mailSender.send(message);
    }
}

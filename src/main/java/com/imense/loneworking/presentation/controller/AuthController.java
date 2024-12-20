package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Authentification.LoginDto;
import com.imense.loneworking.application.dto.Authentification.RegistrationDto;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.application.service.serviceInterface.AuthService;
import com.imense.loneworking.presentation.response.InvalidPinException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    // Registration API
    @PostMapping("auth/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDto registrationDto) {
        try {
            // Register the user
            User registeredUser = authService.registerUser(registrationDto);

            // Return 201 Created on successful registration
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);

        } catch (IllegalArgumentException ex) {
            // Handle case where email is already in use or invalid input
            return new ResponseEntity<>("Email is already in use or invalid input", HttpStatus.CONFLICT);

        } catch (Exception ex) {

            // Return 500 Internal Server Error with exception message
            return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Mobile Login API
    @PostMapping("auth/loginmobile")
    public ResponseEntity<String> loginMobile(@RequestBody LoginDto loginDto) {
        try {
            String response = authService.authenticateUserMobile(loginDto);
            return new ResponseEntity<>(response, HttpStatus.OK);  // Return 200 OK on successful login
        } catch (Exception ex) {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);  // Return 401 Unauthorized on authentication failure
        }
    }

    // Mobile Pin Login API
    @PostMapping("auth/loginmobile/pin")
    public ResponseEntity<String> loginMobilePin(@RequestBody Map<String, String> payload) {
        try {
            String pin = payload.get("pin");
            String response = authService.authenticateUserMobilePin(pin);
            return new ResponseEntity<>(response, HttpStatus.OK);  // Return 200 OK on successful authentication
        } catch (InvalidPinException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);  // Handle Invalid Pin case with 401 Unauthorized
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);  // Handle other errors with 500 Internal Server Error
        }
    }

    // Web Login API
    @PostMapping("auth/loginweb")
    public ResponseEntity<String> loginWeb(@RequestBody LoginDto loginDto) {
        try {
            String response = authService.authenticateUserWeb(loginDto);
            return new ResponseEntity<>(response, HttpStatus.OK);  // Return 200 OK on successful login
        } catch (Exception ex) {
            return new ResponseEntity<>("Authentication failed", HttpStatus.UNAUTHORIZED);  // Handle authentication failure with 401 Unauthorized
        }
    }
}

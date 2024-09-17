
package com.imense.loneworking.application.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imense.loneworking.application.dto.Authentification.RegistrationDto;
import com.imense.loneworking.application.dto.Authentification.LoginDto;
import com.imense.loneworking.application.service.serviceInterface.AuthService;
import com.imense.loneworking.application.service.serviceInterface.SafetyTrackerAuthService;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.infrastructure.security.JwtUtil;
import com.imense.loneworking.presentation.response.InvalidPinException;
import com.imense.loneworking.presentation.response.ExpiredTokenException;
import com.imense.loneworking.presentation.response.InvalidPinException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SafetyTrackerAuthServiceImpl implements SafetyTrackerAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final WebClient webClient;

    public SafetyTrackerAuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                           UserDetailsServiceImpl userDetailsService, WebClient.Builder webClientBuilder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.webClient = webClientBuilder.baseUrl("https://core-dev.safetytracker.ma/api/v1").build();  // External API base URL
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }



//    @Override
//    public User registerUser(RegistrationDto registrationDto) {
//        User user = new User();
//        user.setEmail(registrationDto.getEmail());
//        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
//        user.setRole(registrationDto.getRole());
//        user.setSiteId(Long.valueOf(registrationDto.getSite_id()));
//
//        User savedUser = userRepository.save(user);
//
//        // Generate token with user ID (local generation)
//        String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(registrationDto.getEmail()), savedUser.getId().toString());
//        savedUser.setFcm_token(token);
//
//        return userRepository.save(savedUser);
//    }


    @Override
    public String safetyTrackerAuthenticateUserWeb(LoginDto loginDto) {
        try {
            String externalToken = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/admin/login")
                            .queryParam("email", loginDto.getEmail())
                            .queryParam("password", loginDto.getPassword())
                            .build())  // Build the URI with query parameters
                    .retrieve()
                    .bodyToMono(String.class)  // Get the full response as a String
                    .map(response -> {
                        // Assuming the token is in a field called "token"
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            JsonNode jsonNode = objectMapper.readTree(response);
                            return jsonNode.get("token").asText();  // Extract the token
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to parse token from response", e);
                        }
                    })
                    .block();  // Block until response is received


            return externalToken;  // Return token from external service

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new RuntimeException("Invalid username or password");
            }
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

//    @Override
//    public String authenticateUserMobile(LoginDto loginDto) {
//        try {
//            // Authenticate user via external backend for mobile login
//            String externalToken = webClient.post()
//                    .uri("/auth/login")  // External login endpoint
//                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                    .bodyValue(loginDto)  // Pass email and password
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//
//            // Optionally save external token for local user
//            User user = userRepository.findByEmail(loginDto.getEmail());
//            if (user != null) {
//                user.setFcm_token(externalToken);
//                userRepository.save(user);
//            }
//
//            return externalToken;
//
//        } catch (WebClientResponseException e) {
//            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                throw new RuntimeException("Invalid username or password");
//            }
//            throw new RuntimeException("Authentication failed: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public String authenticateUserMobilePin(String pin) {
//        String username = getCurrentUsername();
//        User user = userRepository.findByEmail(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        // Check if JWT token is expired
//        if (jwtUtil.isTokenExpired(getCurrentToken())) {
//            throw new ExpiredTokenException("Session expired. Please log in again.");
//        }
//
//        // Validate the PIN
//        if (!Objects.equals(user.getPin(), pin)) {
//            throw new InvalidPinException("Invalid PIN");
//        }
//
//        // Generate a new JWT token with user ID (using local JWT logic)
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        return jwtUtil.generateToken(userDetails, user.getId().toString());
//    }

    private String getCurrentToken() {
        String authHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Remove "Bearer " prefix
        }
        return null;
    }
}
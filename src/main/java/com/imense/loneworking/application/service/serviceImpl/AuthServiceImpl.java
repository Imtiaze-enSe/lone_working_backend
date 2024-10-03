package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Authentification.RegistrationDto;
import com.imense.loneworking.application.dto.Authentification.LoginDto;
import com.imense.loneworking.application.service.serviceInterface.AuthService;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.TenantRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.infrastructure.security.JwtUtil;
import com.imense.loneworking.presentation.response.InvalidPinException;

import com.imense.loneworking.presentation.response.ExpiredTokenException;
import com.imense.loneworking.presentation.response.InvalidPinException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final TenantRepository tenantRepository;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
                           TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tenantRepository = tenantRepository;
    }
    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public User registerUser(RegistrationDto registrationDto) {
        // Check if email already exists in the system
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use"); // Or use a custom exception if needed
        }

        User user = new User();
        Optional<Tenant> tenantOptional = tenantRepository.findById(registrationDto.getTenant_id());

        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole());
        user.setSiteId(registrationDto.getSite_id());

        if (tenantOptional.isPresent()) {
            Tenant tenant = tenantOptional.get();
            user.setTenant(tenant);
        }

        User savedUser = userRepository.save(user);

        // Generate token with user ID
        String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(registrationDto.getEmail()), savedUser.getId().toString());
        savedUser.setFcm_token(token);

        return userRepository.save(savedUser);
    }


    @Override
    public String authenticateUserWeb(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        User user = userRepository.findByEmail(loginDto.getEmail());

        if (userDetails.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("User does not have admin privileges");
        }

        // Generate token with user ID
        return jwtUtil.generateToken(userDetails, user.getId().toString());
    }

    @Override
    public String authenticateUserMobile(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        User user = userRepository.findByEmail(loginDto.getEmail());

        // Generate token with user ID
        return jwtUtil.generateToken(userDetails, user.getId().toString());
    }

    @Override
    public String authenticateUserMobilePin(String pin) {
        String username = getCurrentUsername();
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Check if the JWT token is expired
        if (jwtUtil.isTokenExpired(getCurrentToken())) {
            throw new ExpiredTokenException("Session expired. Please log in again.");
        }

        // Validate the PIN
        if (!Objects.equals(user.getPin(), pin)) {
            throw new InvalidPinException("Invalid PIN");
        }

        // Generate a new JWT token with the user ID
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails, user.getId().toString());
    }


    private String getCurrentToken() {
        String authHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Remove "Bearer " prefix
        }
        return null;
    }

}

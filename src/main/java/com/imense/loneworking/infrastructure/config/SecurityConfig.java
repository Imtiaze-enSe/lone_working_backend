package com.imense.loneworking.infrastructure.config;

import com.imense.loneworking.domain.entity.Enum.UserRole;
import com.imense.loneworking.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

import static com.imense.loneworking.domain.entity.Enum.Permission.*;
import static com.imense.loneworking.domain.entity.Enum.UserRole.ADMIN;
import static com.imense.loneworking.domain.entity.Enum.UserRole.WORKER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/location").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/safety-tracker/**").permitAll()
                        .requestMatchers("/api/mobile/**").hasAnyRole(ADMIN.name(), WORKER.name())
                        .requestMatchers(GET, "/api/mobile/**").hasAnyAuthority(ADMIN_READ.name(), WORKER_READ.name())
                        .requestMatchers(POST, "/api/mobile/**").hasAnyAuthority(ADMIN_CREATE.name(), WORKER_CREATE.name())
                        .requestMatchers(PUT, "/api/mobile/**").hasAnyAuthority(ADMIN_UPDATE.name(), WORKER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/mobile/**").hasAnyAuthority(ADMIN_DELETE.name(), WORKER_DELETE.name())
                        .requestMatchers("/api/web/**").hasRole(ADMIN.name())
                        .requestMatchers(GET, "/api/web/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST, "/api/web/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/api/web/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE, "/api/web/**").hasAuthority(ADMIN_DELETE.name())
                        .anyRequest().authenticated())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

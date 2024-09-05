package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.domain.entity.Enum.UserRole;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.infrastructure.security.JwtUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        UserRole userRole = user.getRole();
        List<SimpleGrantedAuthority> authorities = userRole.getAuthorities();


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}

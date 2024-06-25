package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.ClientDto;
import com.imense.loneworking.application.dto.LoginDto;
import com.imense.loneworking.domain.entity.Client;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ClientService {
    Client registerClient(ClientDto clientDto);
    String authenticateClient(LoginDto loginDto);
}

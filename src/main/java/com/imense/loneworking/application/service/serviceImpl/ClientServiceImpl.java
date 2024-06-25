package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.ClientDto;
import com.imense.loneworking.application.dto.LoginDto;
import com.imense.loneworking.application.service.serviceInterface.ClientService;
import com.imense.loneworking.domain.entity.Client;
import com.imense.loneworking.domain.repository.ClientRepository;
import com.imense.loneworking.infrastructure.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Client registerClient(ClientDto clientDto) {
        Client client = new Client();
        client.setEmailClient(clientDto.getEmailClient());
        client.setPasswordClient(passwordEncoder.encode(clientDto.getPasswordClient()));
        client.setLogoClient(clientDto.getLogoClient());
        client.setRaisonSocial(clientDto.getRaisonSocial());
        client.setAdresseDuSiege(clientDto.getAdresseDuSiege());
        return clientRepository.save(client);
    }

    @Override
    public String authenticateClient(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmailClient(), loginDto.getPasswordClient()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmailClient());
        return jwtUtil.generateToken(userDetails);
    }
}

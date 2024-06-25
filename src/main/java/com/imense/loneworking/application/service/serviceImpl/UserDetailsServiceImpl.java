package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.domain.entity.Client;
import com.imense.loneworking.domain.repository.ClientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository clientRepository;

    public UserDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmailClient(email);
        if (client == null) {
            throw new UsernameNotFoundException("Client not found");
        }
        return new org.springframework.security.core.userdetails.User(client.getEmailClient(), client.getPasswordClient(), new ArrayList<>());
    }
}

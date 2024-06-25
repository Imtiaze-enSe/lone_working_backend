package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.ClientDto;
import com.imense.loneworking.application.dto.LoginDto;
import com.imense.loneworking.domain.entity.Client;
import com.imense.loneworking.application.service.serviceInterface.ClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ClientService clientService;
    public AuthController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public Client register(@RequestBody ClientDto clientDto) {
        return clientService.registerClient(clientDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return clientService.authenticateClient(loginDto);
    }
}

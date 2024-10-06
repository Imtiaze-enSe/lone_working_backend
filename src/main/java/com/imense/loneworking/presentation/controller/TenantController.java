package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Tenant.TenantCreationDto;
import com.imense.loneworking.application.service.serviceInterface.TenantService;
import com.imense.loneworking.domain.entity.Tenant;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("tenant/create")
    public ResponseEntity<Tenant> createTenant(@Valid @RequestBody TenantCreationDto tenantCreationDto) {
        Tenant createdTenant = tenantService.createTenant(tenantCreationDto);
        return new ResponseEntity<>(createdTenant, HttpStatus.CREATED);
    }
}
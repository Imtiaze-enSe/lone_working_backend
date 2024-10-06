package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Tenant.TenantCreationDto;
import com.imense.loneworking.domain.entity.Tenant;

public interface TenantService {
    Tenant createTenant(TenantCreationDto tenantCreationDto);
}

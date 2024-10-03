package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Tenant.TenantCreationDto;
import com.imense.loneworking.application.service.serviceInterface.TenantService;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;

    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant createTenant(TenantCreationDto tenantCreationDto) {
        Tenant tenant = new Tenant();
        tenant.setName(tenantCreationDto.getName());
        tenant.setType(tenantCreationDto.getType());
        tenant.setParent_id(tenantCreationDto.getParent_id());
        tenant.setStatus(tenantCreationDto.getStatus());
        tenant.setPresentation_type(tenantCreationDto.getPresentation_type());
        tenant.setContractor_site_id(tenantCreationDto.getContractor_site_id());
        tenant.setEmail(tenantCreationDto.getEmail());
        tenant.setAcronym(tenantCreationDto.getAcronym());
        tenant.setLegal_form(tenantCreationDto.getLegal_form());
        tenant.setTva(tenantCreationDto.getTva());
        tenant.setNace(tenantCreationDto.getNace());
        tenant.setDescription(tenantCreationDto.getDescription());
        tenant.setAddress(tenantCreationDto.getAddress());
        tenant.setN_address(tenantCreationDto.getN_address());
        tenant.setBox(tenantCreationDto.getBox());
        tenant.setZipCode(tenantCreationDto.getZipCode());
        tenant.setCity(tenantCreationDto.getCity());
        tenant.setCountry(tenantCreationDto.getCountry());
        tenant.setPhone(tenantCreationDto.getPhone());
        if (tenantCreationDto.getLogo() != null) {
            tenant.setLogo(Base64.getDecoder().decode(tenantCreationDto.getLogo()));
        } else {
            tenant.setLogo(null); // or handle as needed
        }
        tenant.setWebsite(tenantCreationDto.getWebsite());
        tenant.setMedicName(tenantCreationDto.getMedicName());
        tenant.setMedic_speciality(tenantCreationDto.getMedic_speciality());
        tenant.setMedic_phone(tenantCreationDto.getMedic_phone());
        if (tenantCreationDto.getMedic_photo() != null) {
            tenant.setMedic_photo(Base64.getDecoder().decode(tenantCreationDto.getMedic_photo()));
        } else {
            tenant.setMedic_photo(null); // or handle as needed
        }
        tenant.setN_emergency(tenantCreationDto.getN_emergency());

        return tenantRepository.save(tenant);
    }
}

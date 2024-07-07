package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.SiteCreationDto;
import com.imense.loneworking.application.dto.SiteInfoDto;
import com.imense.loneworking.application.service.serviceInterface.SiteService;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.TenantRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.domain.repository.ZoneRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiteServiceImpl implements SiteService {
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final ZoneRepository zoneRepository;
    private final TenantRepository tenantRepository;

    public SiteServiceImpl(UserRepository userRepository, SiteRepository siteRepository, ZoneRepository zoneRepository,
                           TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.zoneRepository = zoneRepository;
        this.tenantRepository = tenantRepository;
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
    @Override
    public List<SiteInfoDto> getSiteInfo() {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Long siteId = authUser.getSiteId();
        Optional<Site> site = siteRepository.findById(siteId);
        Tenant tenant = site.get().getTenant();
        Integer NbrZones = zoneRepository.countZoneBySite(site);
        List<Site> sites = siteRepository.findSitesByTenant_Id(tenant.getId());

        return sites.stream()
                .map(thisSite -> {
                    SiteInfoDto dto = new SiteInfoDto();
                    dto.setSite_id(siteId);
                    dto.setSiteName(site.get().getName());
                    dto.setCompanyName(site.get().getTenant().getName());
                    dto.setLocation(site.get().getLocation());
                    dto.setNbrZones(NbrZones);
                    dto.setSiteCreatedAt(site.get().getCreated_at());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Site addSite(SiteCreationDto siteCreationDto) {
        Tenant tenant = tenantRepository.findByName(siteCreationDto.getCompanyName());
        if (tenant == null) {
            throw new RuntimeException("Tenant not found");
        }

        Site site = new Site();
        site.setName(siteCreationDto.getSiteName());
        site.setTenant(tenant);
        site.setLocation(siteCreationDto.getLocation());
        site.setPlan2d(siteCreationDto.getPlan2d());
        site.setPlan3d(siteCreationDto.getPlan3d());
        site.setCreated_at(LocalDateTime.now());
        site.setUpdated_at(LocalDateTime.now());

        return siteRepository.save(site);
    }
    @Override
    public Site updateSite(Long siteId, SiteCreationDto siteCreationDto) {
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        Tenant tenant = tenantRepository.findByName(siteCreationDto.getCompanyName());
        if (tenant == null) {
            throw new RuntimeException("Tenant not found");
        }

        site.setName(siteCreationDto.getSiteName());
        site.setLocation(siteCreationDto.getLocation());
        site.setPlan2d(siteCreationDto.getPlan2d());
        site.setPlan3d(siteCreationDto.getPlan3d());
        site.setTenant(tenant);
        site.setUpdated_at(LocalDateTime.now());

        return siteRepository.save(site);
    }
    @Override
    @Transactional
    public void deleteSite(Long siteId) {
        siteRepository.deleteById(siteId);
    }


}
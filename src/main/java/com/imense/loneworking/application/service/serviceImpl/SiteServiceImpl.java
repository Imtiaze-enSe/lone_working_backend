package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.SiteDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.SiteQrCodeDto;
import com.imense.loneworking.application.dto.Site.SiteCreationDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        Tenant tenant = authUser.getTenant();
        List<Site> sites = tenant.getSites();

        return sites.stream().filter(Objects::nonNull)
                .map(thisSite -> {
                    Integer NbrZones = zoneRepository.countZoneBySite(Optional.of(thisSite));
                    SiteInfoDto dto = new SiteInfoDto();
                    dto.setSite_id(thisSite.getId());
                    dto.setSiteName(thisSite.getName());
                    dto.setCompanyName(thisSite.getTenant().getName());
                    dto.setLocation(thisSite.getLocation());
                    dto.setNbrZones(NbrZones);
                    dto.setSiteCreatedAt(thisSite.getCreated_at());
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
        userRepository.deleteUsersBySiteId(siteId);
        siteRepository.deleteById(siteId);
    }


    @Override
    public List<SiteDashboardDto> getSiteInfoDashboard(){
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Tenant tenant = authUser.getTenant();
        List<Site> sites = tenant.getSites();

        return sites.stream()
                .map(thisSite -> {
                    SiteDashboardDto dto = new SiteDashboardDto();
                    dto.setId(thisSite.getId());
                    dto.setName(thisSite.getName());
                    dto.setPlan(thisSite.getPlan2d());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<SiteQrCodeDto> getSiteInfoQrCode() {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Tenant tenant = authUser.getTenant();
        List<Site> sites = tenant.getSites();


        List<SiteQrCodeDto> siteQrCodeDtos=new ArrayList<>();
        for(Site site:sites){
            SiteQrCodeDto siteQrCodeDto= new SiteQrCodeDto();
            siteQrCodeDto.setSiteName(site.getName());
            siteQrCodeDto.setSite_id(site.getId());
            siteQrCodeDtos.add(siteQrCodeDto);
        }
        return siteQrCodeDtos;
    }

}

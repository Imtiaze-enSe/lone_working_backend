package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.ZoneDashboardDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.ZoneService;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.Zone;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.domain.repository.ZoneRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository, UserRepository userRepository, SiteRepository siteRepository) {
        this.zoneRepository = zoneRepository;
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public List<ZoneInfoDto> getSiteZoneInfo() {
        String username = getCurrentUsername();
        User user = userRepository.findByEmail(username);
        Tenant tenant = user.getTenant();
        List<Site> sites = tenant.getSites();

        List<ZoneInfoDto> zoneInfoDtos = new ArrayList<>();

        for (Site site : sites) {
            List<Zone> zones = site.getZones();
            List<ZoneInfoDto> siteZones = zones.stream().map(zone -> {
                ZoneInfoDto dto = new ZoneInfoDto();
                dto.setZone_id(zone.getId());
                dto.setSiteName(site.getName());
                dto.setZoneName(zone.getName());
                dto.setCompanyName(tenant.getName());
                dto.setZoneCreatedAt(zone.getCreated_at());
                dto.setZonePlan(zone.getPlan());
                return dto;
            }).toList();
            zoneInfoDtos.addAll(siteZones);
        }
        return zoneInfoDtos;
    }


    @Override
    public Zone addZone(ZoneCreationDto zoneCreationDto) {
        Site site = siteRepository.findByName(zoneCreationDto.getSiteName());
        if (site == null) {
            throw new RuntimeException("Site not found");
        }
        Zone zone = new Zone();
        zone.setName(zoneCreationDto.getZoneName());
        System.out.println(zoneCreationDto.getPlanZone());
        zone.setPlan(zoneCreationDto.getPlanZone());
        zone.setSite(site);
        zone.setCreated_at(LocalDateTime.now());
        zone.setUpdated_at(LocalDateTime.now());
        zone.setStatus(true);

        return zoneRepository.save(zone);
    }
    @Override
    public Zone updateZone(Long zoneId, ZoneUpdateDto zoneUpdateDto) {
        Site site = siteRepository.findByName(zoneUpdateDto.getSiteName());
        if (site == null) {
            throw new RuntimeException("Site not found");
        }

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        zone.setPlan(zoneUpdateDto.getZonePlan());
        zone.setName(zoneUpdateDto.getZoneName());
        zone.setStatus(zoneUpdateDto.getZoneStatus());
        zone.setNumber(zoneUpdateDto.getZoneNumber());
        zone.setSite(site);

        return zoneRepository.save(zone);
    }
    @Override
    public void deleteZone(Long zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        zoneRepository.delete(zone);
    }

    @Override
    public List<ZoneDashboardDto> getSiteZoneInfoDashboard(Long site_id) {
        List<Zone> zones = zoneRepository.findBySiteId(site_id);

        return zones.stream().map(zone -> {
            Site site = zone.getSite();
            ZoneDashboardDto dto = new ZoneDashboardDto();
            dto.setId(zone.getId());
            dto.setName(zone.getName());
            dto.setSite_id(site.getId());
            dto.setPlan(zone.getPlan());
            return dto;
        }).collect(Collectors.toList());
    }
}

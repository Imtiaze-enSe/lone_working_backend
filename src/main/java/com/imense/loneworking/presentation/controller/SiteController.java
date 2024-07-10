package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Dashboard.SiteDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.SiteQrCodeDto;
import com.imense.loneworking.application.dto.Site.SiteCreationDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
import com.imense.loneworking.application.service.serviceInterface.SiteService;
import com.imense.loneworking.domain.entity.Site;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SiteController {
    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }
    @GetMapping("web/sites")
    public List<SiteInfoDto> getSiteInfo() {
        return siteService.getSiteInfo();
    }

    @PostMapping("web/site")
    public Site addSite(@RequestBody SiteCreationDto siteCreationDto) {
        return siteService.addSite(siteCreationDto);
    }

    @PutMapping("web/site/{id}")
    public Site updateSite(@PathVariable Long id, @RequestBody SiteCreationDto siteCreationDto) {
        return siteService.updateSite(id, siteCreationDto);
    }
    @DeleteMapping("web/site/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        siteService.deleteSite(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("web/dashboard/sites")
    public List<SiteDashboardDto> getSiteInfoDashboard() {
        return siteService.getSiteInfoDashboard();
    }

    @GetMapping("web/qrCode/sites")
    public List<SiteQrCodeDto> getSiteInfoQrCode(){return siteService.getSiteInfoQrCode();}
}

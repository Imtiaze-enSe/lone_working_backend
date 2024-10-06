package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Dashboard.SiteDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.SiteQrCodeDto;
import com.imense.loneworking.application.dto.Site.SiteCreationDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
import com.imense.loneworking.application.service.serviceInterface.SiteService;
import com.imense.loneworking.domain.entity.Site;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SiteController {
    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    // Get list of site info
    @GetMapping("web/sites")
    public ResponseEntity<List<SiteInfoDto>> getSiteInfo() {
        try {
            List<SiteInfoDto> siteInfoList = siteService.getSiteInfo();
            return ResponseEntity.ok(siteInfoList);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add a new site
    @PostMapping("web/site")
    public ResponseEntity<Site> addSite(@RequestBody SiteCreationDto siteCreationDto) {
        try {
            Site site = siteService.addSite(siteCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(site);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // Add a first site
    @PostMapping("tenant/firstSite")
    public ResponseEntity<Site> addFirstSite(@RequestBody SiteCreationDto siteCreationDto) {
        try {
            Site site = siteService.addSite(siteCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(site);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update a site by ID
    @PutMapping("web/site/{id}")
    public ResponseEntity<Site> updateSite(@PathVariable Long id, @RequestBody SiteCreationDto siteCreationDto) {
        try {
            Site site = siteService.updateSite(id, siteCreationDto);
            return ResponseEntity.ok(site);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a site by ID
    @DeleteMapping("web/site/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        try {
            siteService.deleteSite(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get site dashboard info
    @GetMapping("web/dashboard/sites")
    public ResponseEntity<List<SiteDashboardDto>> getSiteInfoDashboard() {
        try {
            List<SiteDashboardDto> dashboardSites = siteService.getSiteInfoDashboard();
            return ResponseEntity.ok(dashboardSites);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get site info for QR Code
    @GetMapping("web/qrCode/sites")
    public ResponseEntity<List<SiteQrCodeDto>> getSiteInfoQrCode() {
        try {
            List<SiteQrCodeDto> qrCodeSites = siteService.getSiteInfoQrCode();
            return ResponseEntity.ok(qrCodeSites);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

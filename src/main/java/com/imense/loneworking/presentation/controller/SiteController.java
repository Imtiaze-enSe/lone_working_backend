package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Dashboard.SiteDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.SiteQrCodeDto;
import com.imense.loneworking.application.dto.Site.SiteCreationDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
import com.imense.loneworking.application.service.serviceInterface.SiteService;
import com.imense.loneworking.application.service.serviceInterface.SiteServiceSynchro;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.SiteSynchro;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SiteController {
    private final SiteService siteService;
    private final SiteServiceSynchro siteServiceSynchro;

    public SiteController(SiteService siteService, SiteServiceSynchro siteServiceSynchro) {
        this.siteService = siteService;
        this.siteServiceSynchro = siteServiceSynchro;
    }

    @GetMapping("/web/tenant/{tenantId}/sites")
    public ResponseEntity<List<SiteInfoDto>> getSiteInfo(
            @PathVariable Long tenantId,  // tenantId as path parameter
            @RequestBody Map<String, String> requestBody) {  // token in body
        String token = requestBody.get("token");
        // Fetch the site information using the tenantId and token
        List<SiteInfoDto> siteInfoList = siteServiceSynchro.getSiteInfo(tenantId);

        // Return the site information wrapped in a ResponseEntity
        return ResponseEntity.ok(siteInfoList);  // Return HTTP 200 with the list of sites
    }

//     Get site dashboard info
    @GetMapping("web/dashboard/{tenantId}/sites")
    public ResponseEntity<List<SiteDashboardDto>> getSiteInfoDashboard(
            @PathVariable Long tenantId,  // tenantId as path parameter
            @RequestBody Map<String, String> requestBody
    ) {
        try {
            String token = requestBody.get("token");
            List<SiteDashboardDto> dashboardSites = siteServiceSynchro.getSiteInfoDashboard(tenantId);
            return ResponseEntity.ok(dashboardSites);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Add a new site
    @PostMapping("/web/site")
    public ResponseEntity<SiteSynchro> addSite(
            @RequestBody SiteCreationDto siteCreationDto
    ) {
        try {
            Mono<SiteSynchro> siteMono = siteServiceSynchro.addSite(siteCreationDto);

            // Wait for the Mono to complete and get the result
            SiteSynchro siteSynchro = siteMono.block();  // Blocking for simplicity, but you can handle this reactively if needed.

            return ResponseEntity.status(HttpStatus.CREATED).body(siteSynchro);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Update a site by ID
    @PutMapping("web/site/{id}")
    public ResponseEntity<Mono<SiteSynchro>> updateSite(@PathVariable Long id, @RequestBody SiteCreationDto siteCreationDto) {
        try {
            Mono<SiteSynchro> site = siteServiceSynchro.editSite(id, siteCreationDto);
            return ResponseEntity.ok(site);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a site by ID
    @DeleteMapping("web/site/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        try {
            siteServiceSynchro.deleteSite(id).block();
            siteServiceSynchro.deletSiteFromDatabase(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get site info for QR Code
    @GetMapping("web/qrCode/tenant/{tenantId}/sites")
    public ResponseEntity<List<SiteQrCodeDto>> getSiteInfoQrCode(
            @PathVariable Long tenantId,  // tenantId as path parameter
            @RequestBody Map<String, String> requestBody
    ) {
        try {
            String token = requestBody.get("token");
            List<SiteQrCodeDto> qrCodeSites = siteServiceSynchro.getSiteInfoQrCode(tenantId);
            return ResponseEntity.ok(qrCodeSites);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

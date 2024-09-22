package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Dashboard.ZoneDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.ZoneQrCodeDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.SafetyTrackerZoneService;
import com.imense.loneworking.application.service.serviceInterface.ZoneService;
import com.imense.loneworking.domain.entity.Zone;
import com.imense.loneworking.domain.entity.ZoneSynchro;
import com.imense.loneworking.domain.repository.ZoneSynchroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/safety-tracker")
public class ZoneController {

    private final ZoneService zoneService;
    private final SafetyTrackerZoneService safetyTrackerZoneService;
    private final ZoneSynchroRepository zoneSynchroRepository;

    public ZoneController(ZoneService zoneService, SafetyTrackerZoneService safetyTrackerZoneService, ZoneSynchroRepository zoneSynchroRepository) {
        this.zoneService = zoneService;
        this.safetyTrackerZoneService=safetyTrackerZoneService;
        this.zoneSynchroRepository = zoneSynchroRepository;
    }

    @GetMapping("web/zones")
    public ResponseEntity<List<ZoneInfoDto>> getSiteZoneInfo() {
        //List<ZoneInfoDto> zones = zoneService.getSiteZoneInfo();
        List<ZoneInfoDto> zones = safetyTrackerZoneService.getSiteZoneInfo();
        return zones != null && !zones.isEmpty() ? ResponseEntity.ok(zones) : ResponseEntity.noContent().build();
    }

    @PostMapping("web/zone")
    public ResponseEntity<Zone> addZone(@RequestBody ZoneCreationDto zoneCreationDto) {
        //Zone newZone = zoneService.addZone(zoneCreationDto);
        ZoneSynchro newZone = safetyTrackerZoneService.addZone(zoneCreationDto);
        return new ResponseEntity<>(new Zone(), HttpStatus.CREATED);
    }

    @PutMapping("web/zone/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable Long id, @RequestBody ZoneUpdateDto zoneUpdateDto) {
//        Zone updatedZone = zoneService.updateZone(id, zoneUpdateDto);
        ZoneSynchro updatedZone = safetyTrackerZoneService.updateZone(id, zoneUpdateDto);
        return updatedZone != null ? ResponseEntity.ok(new Zone()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("web/zone/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        //zoneService.deleteZone(id);
        safetyTrackerZoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("web/dashboard/zones/site_id:{site_id}")
    public ResponseEntity<List<ZoneDashboardDto>> getSiteZoneInfoDashboard(@PathVariable Long site_id) {
        //List<ZoneDashboardDto> zones = zoneService.getSiteZoneInfoDashboard(site_id);
        List<ZoneDashboardDto> zones = safetyTrackerZoneService.getSiteZoneInfoDashboard(site_id);
        return zones != null && !zones.isEmpty() ? ResponseEntity.ok(zones) : ResponseEntity.noContent().build();
    }

    @GetMapping("web/qrCode/zones/site_id:{site_id}")
    public ResponseEntity<List<ZoneQrCodeDto>> getZoneInfoQrCode(@PathVariable Long site_id) {
        //List<ZoneQrCodeDto> zones = zoneService.getZoneInfoQrCode(site_id);
        List<ZoneQrCodeDto> zones = safetyTrackerZoneService.getZoneInfoQrCode(site_id);
        return zones != null && !zones.isEmpty() ? ResponseEntity.ok(zones) : ResponseEntity.noContent().build();
    }

    @GetMapping("mobile/zones/site_id:{site_id}")
    public ResponseEntity<List<ZoneQrCodeDto>> getZoneInfoForMobile(@PathVariable Long site_id) {
        //List<ZoneQrCodeDto> zones = zoneService.getZoneInfoQrCode(site_id);
        List<ZoneQrCodeDto> zones = safetyTrackerZoneService.getZoneInfoQrCode(site_id);
        return zones != null && !zones.isEmpty() ? ResponseEntity.ok(zones) : ResponseEntity.noContent().build();
    }
}

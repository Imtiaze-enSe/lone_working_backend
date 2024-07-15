package com.imense.loneworking.presentation.controller;


import com.imense.loneworking.application.dto.Dashboard.ZoneDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.ZoneQrCodeDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.ZoneService;
import com.imense.loneworking.domain.entity.Zone;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping("web/zones")
    public List<ZoneInfoDto> getSiteZoneInfo() {
        return zoneService.getSiteZoneInfo();
    }

    @PostMapping("web/zone")
    public Zone addZone(@RequestBody ZoneCreationDto zoneCreationDto) {
        return zoneService.addZone(zoneCreationDto);
    }

    @PutMapping("web/zone/{id}")
    public Zone updateZone(@PathVariable Long id, @RequestBody ZoneUpdateDto zoneUpdateDto) {
        return zoneService.updateZone(id, zoneUpdateDto);
    }
    @DeleteMapping("web/zone/{id}")
    public void deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
    }

    @GetMapping("web/dashboard/zones/site_id:{site_id}")
    public List<ZoneDashboardDto> getSiteZoneInfoDashboard(@PathVariable Long site_id) {
        return zoneService.getSiteZoneInfoDashboard(site_id);
    }

    @GetMapping("web/qrCode/zones/site_id:{site_id}")
    public List<ZoneQrCodeDto>getZoneInfoQrCode(@PathVariable Long site_id){
        return zoneService.getZoneInfoQrCode(site_id);
    }
}

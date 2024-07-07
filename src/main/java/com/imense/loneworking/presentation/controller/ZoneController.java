package com.imense.loneworking.presentation.controller;


import com.imense.loneworking.application.dto.ZoneCreationDto;
import com.imense.loneworking.application.dto.ZoneInfoDto;
import com.imense.loneworking.application.dto.ZoneUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.ZoneInfoService;
import com.imense.loneworking.domain.entity.Zone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ZoneController {

    private final ZoneInfoService zoneInfoService;

    public ZoneController(ZoneInfoService zoneInfoService) {
        this.zoneInfoService = zoneInfoService;
    }

    @GetMapping("web/zones")
    public List<ZoneInfoDto> getSiteZoneInfo() {
        return zoneInfoService.getSiteZoneInfo();
    }

    @PostMapping("web/zone")
    public Zone addZone(@RequestBody ZoneCreationDto zoneCreationDto) {
        return zoneInfoService.addZone(zoneCreationDto);
    }
    @PutMapping("web/zone/{id}")
    public Zone updateZone(@PathVariable Long id, @RequestBody ZoneUpdateDto zoneUpdateDto) {
        return zoneInfoService.updateZone(id, zoneUpdateDto);
    }
    @DeleteMapping("web/zone/{id}")
    public void deleteZone(@PathVariable Long id) {
        zoneInfoService.deleteZone(id);
    }
}

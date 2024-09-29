package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Dashboard.ZoneDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.ZoneQrCodeDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.domain.entity.Zone;
import com.imense.loneworking.domain.entity.ZoneSynchro;

import java.util.List;

public interface SafetyTrackerZoneService {
    List<ZoneInfoDto> getSiteZoneInfo();
    ZoneSynchro addZone(ZoneCreationDto zoneCreationDto);
    ZoneSynchro updateZone(Long zoneId, ZoneUpdateDto zoneUpdateDto);
    void deleteZone(Long zoneId);
    List<ZoneDashboardDto> getSiteZoneInfoDashboard(Long site_id);
    List<ZoneQrCodeDto> getZoneInfoQrCode(Long site_id);
}

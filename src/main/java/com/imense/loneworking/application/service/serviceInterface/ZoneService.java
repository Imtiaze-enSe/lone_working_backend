package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Dashboard.ZoneDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.ZoneQrCodeDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.domain.entity.Zone;

import java.util.List;

public interface ZoneService {
    List<ZoneInfoDto> getSiteZoneInfo();
    Zone addZone(ZoneCreationDto zoneCreationDto);
    Zone updateZone(Long zoneId, ZoneUpdateDto zneUpdateDto);
    void deleteZone(Long zoneId);

    List<ZoneDashboardDto> getSiteZoneInfoDashboard(Long site_id);

    List<ZoneQrCodeDto> getZoneInfoQrCode(Long site_id);

}

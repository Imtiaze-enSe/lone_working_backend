package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.ZoneCreationDto;
import com.imense.loneworking.application.dto.ZoneInfoDto;
import com.imense.loneworking.application.dto.ZoneUpdateDto;
import com.imense.loneworking.domain.entity.Zone;

import java.util.List;

public interface ZoneInfoService {
    List<ZoneInfoDto> getSiteZoneInfo();
    Zone addZone(ZoneCreationDto zoneCreationDto);
    Zone updateZone(Long zoneId, ZoneUpdateDto zneUpdateDto);
    void deleteZone(Long zoneId);
}

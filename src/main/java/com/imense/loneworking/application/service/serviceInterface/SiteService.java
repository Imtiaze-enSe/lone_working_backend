package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.SiteCreationDto;
import com.imense.loneworking.application.dto.SiteInfoDto;
import com.imense.loneworking.application.dto.ZoneInfoDto;
import com.imense.loneworking.domain.entity.Site;

import java.util.List;

public interface SiteService {
    List<SiteInfoDto> getSiteInfo();
    Site addSite(SiteCreationDto siteDto);
    Site updateSite(Long siteId, SiteCreationDto siteCreationDto);
    void deleteSite(Long siteId);
}

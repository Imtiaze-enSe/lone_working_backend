package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Dashboard.SiteDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.SiteQrCodeDto;
import com.imense.loneworking.application.dto.Site.SiteCreationDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
import com.imense.loneworking.domain.entity.Site;

import java.util.List;

public interface SiteService {
    List<SiteInfoDto> getSiteInfo();
    Site addSite(SiteCreationDto siteDto);
    Site updateSite(Long siteId, SiteCreationDto siteCreationDto);
    void deleteSite(Long siteId);

    List<SiteDashboardDto> getSiteInfoDashboard();

    List<SiteQrCodeDto> getSiteInfoQrCode();
}

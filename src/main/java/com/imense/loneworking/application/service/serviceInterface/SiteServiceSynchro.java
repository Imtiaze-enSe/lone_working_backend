package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Dashboard.SiteDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.SiteQrCodeDto;
import com.imense.loneworking.application.dto.Site.SiteCreationDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.SiteSynchro;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface SiteServiceSynchro {
    List<SiteInfoDto> getSiteInfo(Long tenantId, String token);
    List<SiteDashboardDto> getSiteInfoDashboard(Long tenantId, String token);
    List<SiteQrCodeDto> getSiteInfoQrCode(Long tenantId, String token);
    Mono<SiteSynchro> addSite(SiteCreationDto siteCreationDto);
}

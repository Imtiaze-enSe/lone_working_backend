package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.SiteDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.SiteQrCodeDto;
import com.imense.loneworking.application.dto.Site.SiteCreationDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
import com.imense.loneworking.application.service.serviceInterface.SiteServiceSynchro;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.SiteSynchro;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.SiteSynchroRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SiteServiceImplSynchro implements SiteServiceSynchro {

    private final WebClient webClient;
    private final SiteSynchroRepository siteSynchroRepository;

    public SiteServiceImplSynchro(WebClient.Builder webClientBuilder, SiteRepository siteRepository,
                                  SiteSynchroRepository siteSynchroRepository) {
        this.webClient = webClientBuilder.baseUrl("https://core-dev.safetytracker.ma/api/v1").build();
        this.siteSynchroRepository = siteSynchroRepository;
    }

    // Fetch site info and map to SiteInfoDto using tenant_id and token
    @Override
    public List<SiteInfoDto> getSiteInfo(Long tenantId, String token) {
        // Fetch the data from the Safety Tracker API
        List<Map<String, Object>> siteDataList = fetchSiteData(tenantId, token).block();

        // Transform the response into SiteInfoDto format
        return siteDataList.stream()
                .map(this::mapToSiteInfoDto)
                .collect(Collectors.toList());
    }

    // Fetch data from the Safety Tracker API using tenant_id
    private Mono<List<Map<String, Object>>> fetchSiteData(Long tenantId, String token) {
        String url = "/loneworking/sites/" + tenantId;

        return webClient
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>) response.get("data"));
    }

    // Map the raw API data to SiteInfoDto
    private SiteInfoDto mapToSiteInfoDto(Map<String, Object> siteData) {
        SiteInfoDto siteInfoDto = new SiteInfoDto();

        // Mapping directly from the API response
        siteInfoDto.setSite_id(Long.valueOf(siteData.get("id").toString()));
        siteInfoDto.setSiteName((String) siteData.get("name"));
        siteInfoDto.setCompanyName((String) ((Map<String, Object>) siteData.get("tenant")).get("name"));
        siteInfoDto.setLocation((String) siteData.get("address"));
        siteInfoDto.setNbrZones((Integer) siteData.get("nbr_zones"));
        siteInfoDto.setSiteCreatedAt((String) siteData.get("created_at"));

        return siteInfoDto;
    }


    @Override
    public List<SiteDashboardDto> getSiteInfoDashboard(Long tenantId, String token){
        // Fetch the data from the Safety Tracker API
        List<Map<String, Object>> siteDataList = fetchSiteData(tenantId, token).block();

        // Transform the response into SiteInfoDto format
        assert siteDataList != null;
        return siteDataList.stream()
                .map(this::mapToSiteDashboardDto)
                .collect(Collectors.toList());
    }
    // Map the raw API data to SiteInfoDto
    private SiteDashboardDto mapToSiteDashboardDto(Map<String, Object> siteData) {
        SiteDashboardDto siteDashboardDto = new SiteDashboardDto();
        Optional<SiteSynchro> site = siteSynchroRepository.findByRefId(Long.valueOf(siteData.get("id").toString()));
        if (site.isPresent() && site.get().getPlan2d() != null){
            siteDashboardDto.setPlan(site.get().getPlan2d());
        }
        // Mapping directly from the API response
        siteDashboardDto.setId(Long.valueOf(siteData.get("id").toString()));
        siteDashboardDto.setName((String) siteData.get("name"));

        return siteDashboardDto;
    }

    @Override
    public List<SiteQrCodeDto> getSiteInfoQrCode(Long tenantId, String token) {
        // Fetch the data from the Safety Tracker API
        List<Map<String, Object>> siteDataList = fetchSiteData(tenantId, token).block();

        // Transform the response into SiteInfoDto format
        assert siteDataList != null;
        return siteDataList.stream()
                .map(this::mapToSiteQrCodeDto)
                .collect(Collectors.toList());
    }
    // Map the raw API data to SiteInfoDto
    private SiteQrCodeDto mapToSiteQrCodeDto(Map<String, Object> siteData) {
        SiteQrCodeDto siteQrCodeDto = new SiteQrCodeDto();
        // Mapping directly from the API response
        siteQrCodeDto.setSite_id(Long.valueOf(siteData.get("id").toString()));
        siteQrCodeDto.setSiteName((String) siteData.get("name"));

        return siteQrCodeDto;
    }

//    @Override
//    public Site addSite(SiteCreationDto siteCreationDto) {
//
//        Site site = new Site();
//        site.setName(siteCreationDto.getSiteName());
//        site.setLocation(siteCreationDto.getLocation());
//        site.setPlan2d(siteCreationDto.getPlan2d());
//        site.setPlan3d(siteCreationDto.getPlan3d());
//        site.setCreated_at(LocalDateTime.now());
//        site.setUpdated_at(LocalDateTime.now());
//
//        return siteRepository.save(site);
//    }

    @Override
    public Mono<SiteSynchro> addSite(SiteCreationDto siteCreationDto) {
        String url = "/client/sites";
        String token = "1392|8PoemSJt1EOkUapqYVH2JHHVJwkSOkwSeLaWS44A71ee5a6c";
        // Extract the site data from the request body
        System.out.println(siteCreationDto);
        // Create a map with only name and address
        // Build form-data payload using MultipartBodyBuilder
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("site[name]", siteCreationDto.getSiteName());
        bodyBuilder.part("site[address]", siteCreationDto.getLocation());

        return webClient
                .post()
                .uri(url)
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(Map.class)  // Retrieve the response as a map to extract fields
                .publishOn(Schedulers.boundedElastic())  // Retrieve the response as a map to extract fields
                .handle((response, sink) -> {
                    // Check if the API call was successful
                    System.out.println(response);
                    if (Boolean.TRUE.equals(response.get("success"))) {
                        Map<String, Object> siteResponseData = (Map<String, Object>) response.get("site");
                        Long id = Long.valueOf(siteResponseData.get("id").toString());

                        // Create SiteSynchro with the returned id
                        SiteSynchro siteSynchro = new SiteSynchro();
                        siteSynchro.setRefId(id);
                        siteSynchro.setPlan2d(siteCreationDto.getPlan2d());
                        // Save the SiteSynchro entity
                        siteSynchroRepository.save(siteSynchro);

                    } else {
                        sink.error(new RuntimeException("Failed to add site: " + response.get("message")));
                    }
                });
    }


}

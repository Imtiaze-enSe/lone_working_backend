package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.service.serviceInterface.SafetyTrackerService;
import com.imense.loneworking.domain.entity.SiteSynchro;
import com.imense.loneworking.domain.entity.UserSynchro;
import com.imense.loneworking.domain.entity.ZoneSynchro;
import com.imense.loneworking.domain.repository.SiteSynchroRepository;
import com.imense.loneworking.domain.repository.UserSynchroRepository;
import com.imense.loneworking.domain.repository.ZoneSynchroRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class SafetyTrackerServiceImpl implements SafetyTrackerService {

    private final WebClient webClient;
    private final SiteSynchroRepository siteSynchroRepository;
    private final ZoneSynchroRepository zoneSynchroRepository;
    private final UserSynchroRepository userSynchroRepository;

    public SafetyTrackerServiceImpl(WebClient.Builder webClientBuilder,
                                    SiteSynchroRepository siteSynchroRepository,
                                    ZoneSynchroRepository zoneSynchroRepository,
                                    UserSynchroRepository userSynchroRepository) {
        this.webClient = webClientBuilder.baseUrl("https://core-dev.safetytracker.ma/api/v1").build();
        this.siteSynchroRepository = siteSynchroRepository;
        this.zoneSynchroRepository = zoneSynchroRepository;
        this.userSynchroRepository = userSynchroRepository;
    }

    @Override
    public void synchronizeData(String token) {
        // Fetch data from Safety Tracker using WebClient
        fetchSafetyTrackerData(token).subscribe(data -> {
            // Step 3: Process data and map to entities (only for sites, zones, and users)
            for (Map<String, Object> tenant : data) {

                // Process Sites
                List<Map<String, Object>> sites = (List<Map<String, Object>>) tenant.get("sites");
                for (Map<String, Object> siteData : sites) {
                    Long siteId = Long.valueOf(siteData.get("id").toString());

                    // Directly map geometry fields (plan2d, plan3d)
                    Geometry plan2d = (Geometry) siteData.get("plan2d");
                    Geometry plan3d = (Geometry) siteData.get("plan3d");

                    // Synchronize SiteSynchro
                    SiteSynchro site = siteSynchroRepository.findByRefId(siteId)
                            .orElse(new SiteSynchro());
                    site.setRefId(siteId);
                    site.setPlan2d(plan2d);
                    site.setPlan3d(plan3d);
                    siteSynchroRepository.save(site);

                    // Process Zones within Site
                    List<Map<String, Object>> zones = (List<Map<String, Object>>) siteData.get("zones");
                    for (Map<String, Object> zoneData : zones) {
                        Long zoneId = Long.valueOf(zoneData.get("id").toString());

                        // Directly map geometry field (plan)
                        Geometry plan = (Geometry) zoneData.get("plan");

                        // Synchronize ZoneSynchro
                        ZoneSynchro zone = zoneSynchroRepository.findByRefId(zoneId)
                                .orElse(new ZoneSynchro());
                        zone.setRefId(zoneId);
                        zone.setPlan(plan);
                        zoneSynchroRepository.save(zone);
                    }

                    // Process Users (if available)
                    List<Map<String, Object>> users = (List<Map<String, Object>>) siteData.get("users");

                    // Check if users is null before iterating over it
                    if (users != null) {
                        for (Map<String, Object> userData : users) {
                            Long userId = Long.valueOf(userData.get("id").toString());

                            // Directly map geometry field (position)
                            Geometry position = (Geometry) userData.get("position");

                            // Synchronize UserSynchro
                            UserSynchro user = userSynchroRepository.findByRefId(userId)
                                    .orElse(new UserSynchro());
                            user.setRefId(userId);
                            user.setPosition(position);
                            userSynchroRepository.save(user);
                        }
                    } else {
                        // Log or handle the case where users is null, if necessary
                        System.out.println("No users found for tenant ID: " + tenant.get("id"));
                    }
                }
            }
        });
    }

    private Mono<List<Map<String, Object>>> fetchSafetyTrackerData(String token) {
        return webClient
                .get()
                .uri("/synchro")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>) response.get("data"));
    }
}

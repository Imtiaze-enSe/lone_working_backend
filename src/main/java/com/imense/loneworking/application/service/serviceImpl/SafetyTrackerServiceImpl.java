package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.service.serviceInterface.SafetyTrackerService;
import com.imense.loneworking.domain.entity.SiteSynchro;
import com.imense.loneworking.domain.entity.UserSynchro;
import com.imense.loneworking.domain.entity.ZoneSynchro;
import com.imense.loneworking.domain.repository.SiteSynchroRepository;
import com.imense.loneworking.domain.repository.UserSynchroRepository;
import com.imense.loneworking.domain.repository.ZoneSynchroRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SafetyTrackerServiceImpl implements SafetyTrackerService {
    private final RestTemplate restTemplate;
    private final SiteSynchroRepository siteSynchroRepository;
    private final ZoneSynchroRepository zoneSynchroRepository;
    private final UserSynchroRepository userSynchroRepository;

    public SafetyTrackerServiceImpl(RestTemplate restTemplate, SiteSynchroRepository siteSynchroRepository,
                                    ZoneSynchroRepository zoneSynchroRepository, UserSynchroRepository userSynchroRepository) {
        this.restTemplate = restTemplate;
        this.siteSynchroRepository = siteSynchroRepository;
        this.zoneSynchroRepository = zoneSynchroRepository;
        this.userSynchroRepository = userSynchroRepository;
    }

    @Override
    public void synchronizeData(String token) {

        // Step 2: Fetch data from Safety Tracker
        List<Map<String, Object>> data = fetchSafetyTrackerData(token);

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
    }

    private List<Map<String, Object>> fetchSafetyTrackerData(String token) {
        String url = "https://core-dev.safetytracker.ma/api/v1/synchro";

        // Set the Authorization header with the token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Use GET instead of POST
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map body = response.getBody();
            return (List<Map<String, Object>>) body.get("data"); // Assuming "data" contains the list of items
        } else {
            throw new RuntimeException("Failed to fetch data from Safety Tracker");
        }
    }


}

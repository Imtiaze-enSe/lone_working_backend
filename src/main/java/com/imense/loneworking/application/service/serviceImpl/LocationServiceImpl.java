package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.LocationService;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.SiteRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.imense.loneworking.domain.repository.UserRepository;

import java.util.*;

@Service
public class LocationServiceImpl implements LocationService {

    private final UserRepository userRepository;
    private final GeometryFactory geometryFactory;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SiteRepository siteRepository;

    public LocationServiceImpl(UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate, SiteRepository siteRepository) {
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.siteRepository = siteRepository;
        this.geometryFactory = new GeometryFactory();
    }

    @Override
    public void processLocationUpdate(LocationUpdateDto locationUpdate) {
        Optional<User> user = userRepository.findById(locationUpdate.getUser_id());
        if (user.isPresent()) {
            Long userSiteId = user.get().getSiteId();
            Optional<Site> userSite = siteRepository.findById(userSiteId);
            if (userSite.isPresent()){
                Point point = geometryFactory.createPoint(new Coordinate(locationUpdate.getLongitude(), locationUpdate.getLatitude()));
                // Check if the user's location is within the site's plan
                System.out.println(locationUpdate);
                System.out.println(userSite.get().getPlan2d());
                System.out.println(point);
                System.out.println(userSite.get().getPlan2d().covers(point));
                System.out.println("Before");
                if (userSite.get().getPlan2d() != null && userSite.get().getPlan2d().contains(point)) {
                    System.out.println("After");
                    if (!Objects.equals(locationUpdate.getUser_status(), "Disconnected")) {
                        user.get().setPosition(point);
                    }
                    user.get().setStatus(locationUpdate.getUser_status());
                    userRepository.save(user.get());
                    // Broadcast location update by site
                    simpMessagingTemplate.convertAndSend(
                            "/topic/locations/site/" + locationUpdate.getSite_id(),
                            locationUpdate
                    );
                    // Broadcast location update to user-specific channel
                    simpMessagingTemplate.convertAndSend(
                            "/topic/locations/user/" + locationUpdate.getUser_id(),
                            locationUpdate
                    );
                }
            }
        }
    }
}

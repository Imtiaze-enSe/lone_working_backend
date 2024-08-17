package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.LocationService;
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

    public LocationServiceImpl(UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.geometryFactory = new GeometryFactory();
    }

    @Override
    public void processLocationUpdate(LocationUpdateDto locationUpdate) {
        Optional<User> user = userRepository.findById(locationUpdate.getUser_id());
        if (user.isPresent()) {
            if (!Objects.equals(locationUpdate.getUser_status(), "Disconnected")) {
                Point point = geometryFactory.createPoint(new Coordinate(locationUpdate.getLongitude(), locationUpdate.getLatitude()));
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

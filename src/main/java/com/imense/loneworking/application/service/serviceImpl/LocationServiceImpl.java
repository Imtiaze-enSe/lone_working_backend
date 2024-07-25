package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.LocationService;
import com.imense.loneworking.domain.entity.User;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.infrastructure.websocket.WebSocketService;

import java.util.Optional;

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

//    @Override
//    public void processLocationUpdate(LocationUpdateDto locationUpdate) {
//        System.out.println("Processing location update: " + locationUpdate);  // Add this line
//        // Create a Point geometry from the latitude and longitude
//        Point point = geometryFactory.createPoint(new Coordinate(locationUpdate.getLongitude(), locationUpdate.getLatitude()));
//        System.out.println(point);
//        // Update the user's position in the database
//        User user = userRepository.findByEmail(locationUpdate.getUserEmail());
//        if (user != null){
//            user.setPosition(point);
//            userRepository.save(user);
//        }
//        // Forward the location update to the web frontend
//        webSocketService.sendLocationUpdate(locationUpdate);
//    }

    @Override
    public void processLocationUpdate(LocationUpdateDto locationUpdate) {
        Point point = geometryFactory.createPoint(new Coordinate(locationUpdate.getLongitude(), locationUpdate.getLatitude()));
        Optional<User> user = userRepository.findById(locationUpdate.getUser_id());
        if (user.isPresent()) {
            user.get().setPosition(point);
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

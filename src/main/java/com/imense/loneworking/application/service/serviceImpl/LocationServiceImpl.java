package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.LocationService;
import com.imense.loneworking.domain.entity.User;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.infrastructure.websocket.WebSocketService;

import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private final UserRepository userRepository;
    private final WebSocketService webSocketService;
    private final GeometryFactory geometryFactory;

    public LocationServiceImpl(UserRepository userRepository, WebSocketService webSocketService) {
        this.userRepository = userRepository;
        this.webSocketService = webSocketService;
        this.geometryFactory = new GeometryFactory();
    }

    @Override
    public void processLocationUpdate(LocationUpdateDto locationUpdate) {
        System.out.println("Processing location update: " + locationUpdate);  // Add this line
        // Create a Point geometry from the latitude and longitude
        Point point = geometryFactory.createPoint(new Coordinate(locationUpdate.getLongitude(), locationUpdate.getLatitude()));
        System.out.println(point);
        // Update the user's position in the database
        Optional<User> user = userRepository.findById(locationUpdate.getUserId());
        if (user.isPresent()){
            user.get().setPosition(point);
            userRepository.save(user.get());
        }
        // Forward the location update to the web frontend
        webSocketService.sendLocationUpdate(locationUpdate);
    }
}

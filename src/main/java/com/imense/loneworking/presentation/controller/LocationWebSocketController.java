package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Worker.LocationUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.LocationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LocationWebSocketController {

    private final LocationService locationService;

    public LocationWebSocketController(LocationService locationService) {
        this.locationService = locationService;
    }

    @MessageMapping("/sendLocation")
    public void handleLocationUpdate(LocationUpdateDto locationUpdate) {
        locationService.processLocationUpdate(locationUpdate);
    }
}
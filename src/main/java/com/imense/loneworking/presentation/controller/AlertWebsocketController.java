package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Alert.AlertCreationDto;
import com.imense.loneworking.application.service.serviceInterface.AlertService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlertWebsocketController {
    private final AlertService alertService;

    public AlertWebsocketController(AlertService alertService) {
        this.alertService = alertService;
    }

    @MessageMapping("/sendAlert")
    public void addAlert(@Payload AlertCreationDto alertCreationDto){
        alertService.sendAlert(alertCreationDto);
    }
}

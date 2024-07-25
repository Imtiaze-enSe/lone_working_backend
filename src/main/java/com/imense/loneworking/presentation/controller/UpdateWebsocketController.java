package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Update.UpdateCreationDto;
import com.imense.loneworking.application.service.serviceInterface.UpdateService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateWebsocketController {
    private final UpdateService updateService;

    public UpdateWebsocketController(UpdateService updateService) {
        this.updateService = updateService;
    }

    @MessageMapping("/sendUpdate")
    public void sendUpdate(@Payload UpdateCreationDto updateCreationDto){
        updateService.sendUpdate(updateCreationDto);
    }
}

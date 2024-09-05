package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Update.UpdateInfoDto;
import com.imense.loneworking.application.service.serviceInterface.UpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UpdateController {
    private final UpdateService updateService;

    public UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    // Get updates for web by ID
    @GetMapping("/web/updates/{id}")
    public ResponseEntity<List<UpdateInfoDto>> getUpdates(@PathVariable Long id) {
        try {
            List<UpdateInfoDto> updates = updateService.getUpdates(id);
            return ResponseEntity.ok(updates);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }

    // Get updates for mobile by ID
    @GetMapping("/mobile/alert/updates/{id}")
    public ResponseEntity<List<UpdateInfoDto>> getUpdatesMobile(@PathVariable Long id) {
        try {
            List<UpdateInfoDto> updates = updateService.getUpdates(id);
            return ResponseEntity.ok(updates);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error
        }
    }
}

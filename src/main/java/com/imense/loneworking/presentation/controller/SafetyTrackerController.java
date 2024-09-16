package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.service.serviceInterface.SafetyTrackerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/safety-tracker")
public class SafetyTrackerController {

    private final SafetyTrackerService safetyTrackerService;

    public SafetyTrackerController(SafetyTrackerService safetyTrackerService) {
        this.safetyTrackerService = safetyTrackerService;
    }

    // Endpoint to trigger synchronization with token as header

    @PostMapping("/synchronize")
    public ResponseEntity<Void> synchronizeData(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        safetyTrackerService.synchronizeData(token);
        return ResponseEntity.ok().build();  // Return HTTP 200 on success
    }
}

package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Alert.AlertTableDto;
import com.imense.loneworking.application.dto.Alert.UserInfoAlertDto;
import com.imense.loneworking.application.service.serviceInterface.AlertService;
import com.imense.loneworking.domain.entity.Alert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // Fetching all alerts for the table
    @GetMapping("/web/alerts")
    public ResponseEntity<List<AlertTableDto>> getAlertForTable() {
        try {
            List<AlertTableDto> alerts = alertService.getAlertForTable();
            return ResponseEntity.ok(alerts);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Deleting an alert by ID
    @DeleteMapping("/web/alert/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        try {
            alertService.deleteAlert(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Fetching user information for a specific alert
    @GetMapping("/web/alert/user/{id}")
    public ResponseEntity<UserInfoAlertDto> getUserForAlert(@PathVariable Long id) {
        try {
            UserInfoAlertDto userInfo = alertService.getUserForAlert(id);
            return ResponseEntity.ok(userInfo);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Closing an alert
    @PutMapping("/web/alert/{id}")
    public ResponseEntity<Alert> closeAlert(@PathVariable Long id) {
        try {
            Alert closedAlert = alertService.closeAlert(id);
            return ResponseEntity.ok(closedAlert);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Fetching alert history for a mobile user
    @GetMapping("/mobile/user/alerts")
    public ResponseEntity<List<AlertTableDto>> getAlertHistoryForUser() {
        try {
            List<AlertTableDto> alertHistory = alertService.getAlertHistoryForUser();
            return ResponseEntity.ok(alertHistory);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

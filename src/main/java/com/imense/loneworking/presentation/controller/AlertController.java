package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Alert.AlertTableDto;
import com.imense.loneworking.application.service.serviceInterface.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/api")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/web/alerts")
    public List<AlertTableDto> getAlertForTable(){
        return alertService.getAlertForTable();
    }
}

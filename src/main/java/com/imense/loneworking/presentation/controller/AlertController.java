package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Alert.AlertTableDto;
import com.imense.loneworking.application.dto.Alert.UserInfoAlertDto;
import com.imense.loneworking.application.service.serviceInterface.AlertService;
import com.imense.loneworking.domain.entity.Alert;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/web/alert/{id}")
    public void deleteAlert(@PathVariable Long id){
        alertService.deleteAlert(id);
    }

    @GetMapping("/web/alert/user/{id}")
    public UserInfoAlertDto getUserForAlert(@PathVariable Long id){
        return alertService.getUserForAlert(id);
    }

    @PutMapping("/web/alert/{id}")
    public Alert closeAlert(@PathVariable Long id){
        return alertService.closeAlert(id);
    }

    @GetMapping("/mobile/user/alerts")
    public List<AlertTableDto> getAlertHistoryForUser(){
        return alertService.getAlertHistoryForUser();
    }
}

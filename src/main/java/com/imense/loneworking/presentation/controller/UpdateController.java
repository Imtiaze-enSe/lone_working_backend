package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Update.UpdateInfoDto;
import com.imense.loneworking.application.service.serviceInterface.UpdateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UpdateController {
    private final UpdateService updateService;

    public UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    @GetMapping("/web/updates/{id}")
    public List<UpdateInfoDto> getUpdates(@PathVariable Long id){
        return updateService.getUpdates(id);
    }


}

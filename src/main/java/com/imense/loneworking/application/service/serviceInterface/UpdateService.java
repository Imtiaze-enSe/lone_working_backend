package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Update.UpdateCreationDto;
import com.imense.loneworking.application.dto.Update.UpdateInfoDto;

import java.util.List;

public interface UpdateService {
    void sendUpdate(UpdateCreationDto updateCreationDto);
    List<UpdateInfoDto> getUpdates(Long alert_id);
}

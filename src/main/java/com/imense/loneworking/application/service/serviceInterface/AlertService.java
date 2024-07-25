package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Alert.AlertCreationDto;
import com.imense.loneworking.application.dto.Alert.AlertTableDto;
import com.imense.loneworking.application.dto.Alert.UserInfoAlertDto;

import java.util.List;

public interface AlertService {
    List<AlertTableDto> getAlertForTable();
    void sendAlert(AlertCreationDto alertCreationDto);

    void deleteAlert(Long alertId);
    UserInfoAlertDto getUserForAlert(Long idAlert);
}

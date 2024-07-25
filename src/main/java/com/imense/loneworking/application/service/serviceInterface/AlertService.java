package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Alert.AlertCreationDto;
import com.imense.loneworking.application.dto.Alert.AlertTableDto;
import com.imense.loneworking.application.dto.Alert.AlertTrackerDto;
import com.imense.loneworking.application.dto.Alert.UserInfoAlertDto;
import com.imense.loneworking.domain.entity.Alert;

import java.util.List;

public interface AlertService {
    List<AlertTableDto> getAlertForTable();
    AlertTableDto sendAlert(AlertCreationDto alertCreationDto);

    void deleteAlert(Long alertId);
    UserInfoAlertDto getUserForAlert(Long idAlert);

    Alert closeAlert(Long idAlert);

}

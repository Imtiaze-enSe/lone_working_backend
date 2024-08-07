package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Alert.*;
import com.imense.loneworking.domain.entity.Alert;

import java.util.List;

public interface AlertService {
    List<AlertTableDto> getAlertForTable();
    AlertTableDto sendAlert(AlertCreationDto alertCreationDto);

    void deleteAlert(Long alertId);
    UserInfoAlertDto getUserForAlert(Long idAlert);
    List<AlertTableDto> getAlertHistoryForUser();

    Alert closeAlert(Long idAlert);

}

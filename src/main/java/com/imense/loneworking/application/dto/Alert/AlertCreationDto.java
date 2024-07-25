package com.imense.loneworking.application.dto.Alert;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class AlertCreationDto {
    private String alert_status;
    private String alert_type;
    private int duration;
    private String alert_created_by;
}

package com.imense.loneworking.application.dto.Alert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
public class AlertCreationDto {
    private String alert_status;
    private String alert_type;
    private int duration;
    private String alert_created_by;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime alert_created_at;
}
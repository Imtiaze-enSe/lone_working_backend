package com.imense.loneworking.application.dto.Update;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UpdateCreationDto {
    private Long alert_id;
    private String title;
    private String message;
    private Integer extended_duration;
}

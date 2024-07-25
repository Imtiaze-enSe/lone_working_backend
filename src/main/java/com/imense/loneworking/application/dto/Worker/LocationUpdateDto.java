package com.imense.loneworking.application.dto.Worker;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LocationUpdateDto {
    private Long user_id;
    private Long site_id;
    private double latitude;
    private double longitude;
}

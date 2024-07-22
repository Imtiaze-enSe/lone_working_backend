package com.imense.loneworking.application.dto.Worker;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LocationUpdateDto {
    private String userEmail;
    private double latitude;
    private double longitude;
}

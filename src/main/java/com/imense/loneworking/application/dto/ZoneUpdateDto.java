package com.imense.loneworking.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class ZoneUpdateDto {
    private String zoneNumber;
    private String zoneName;
    private Boolean zoneStatus;
    private Geometry ZonePlan;
    private String siteName;
}

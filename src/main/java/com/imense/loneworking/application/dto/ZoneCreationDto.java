package com.imense.loneworking.application.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class ZoneCreationDto {
    private String zoneName;
    private String siteName;
    private Geometry planZone;

}

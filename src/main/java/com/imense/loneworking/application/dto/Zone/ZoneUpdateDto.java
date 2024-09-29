package com.imense.loneworking.application.dto.Zone;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class ZoneUpdateDto {
    private Long id;
    private String zoneNumber; //  remove
    private String zoneName;
    private Boolean zoneStatus; // remove
    private Geometry planZone;
    private String siteName; // site_id
}

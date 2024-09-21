package com.imense.loneworking.application.dto.Zone;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class ZoneCreationDto {
    private String zoneName;
    private String siteName; // site_id
    private Geometry planZone;

}

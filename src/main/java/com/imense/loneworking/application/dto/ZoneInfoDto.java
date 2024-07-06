package com.imense.loneworking.application.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ZoneInfoDto {
    private Long zone_id;
    private String siteName;
    private String zoneName;
    private String companyName;
    private LocalDateTime zoneCreatedAt;
    private Geometry zonePlan;
}

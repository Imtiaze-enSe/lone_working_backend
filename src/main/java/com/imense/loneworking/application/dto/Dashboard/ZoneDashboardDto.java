package com.imense.loneworking.application.dto.Dashboard;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class ZoneDashboardDto {
    private Long id;
    private String name;
    private Long site_id;
    private Geometry plan;
}

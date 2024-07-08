package com.imense.loneworking.application.dto.Dashboard;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class SiteDashboardDto {
    private Long id;
    private String name;
    private Geometry plan;
}

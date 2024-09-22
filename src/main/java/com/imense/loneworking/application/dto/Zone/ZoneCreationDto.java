package com.imense.loneworking.application.dto.Zone;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class ZoneCreationDto {
    //old ones
    private String zoneName;
    private String siteName;
    // both
    private Geometry planZone;
    //new ones
    private String name;
    private String number;
    private int siteId;
    private List<Integer> levels = new ArrayList<>();
}

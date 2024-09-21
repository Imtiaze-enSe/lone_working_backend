package com.imense.loneworking.application.dto.Site;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class SiteCreationDto {
    private String SiteName;
    private String companyName; // Int company id
    private String location; // adress
    private Geometry plan2d;
    private Geometry plan3d;
}

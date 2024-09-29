package com.imense.loneworking.application.dto.Site;

import org.locationtech.jts.geom.Geometry;

public class SiteUpdateDto {
    private Long id;
    private String SiteName;
    private String companyName; // Int company id
    private String location; // adress
    private Geometry plan2d;
    private Geometry plan3d;
}

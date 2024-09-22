package com.imense.loneworking.application.dto.Site;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.n52.jackson.datatype.jts.GeometryDeserializer;

@Data
@Getter
@Setter
public class SiteCreationDto {
    private String SiteName;
    private String companyName; // Int company id
    private String location; // adress

    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry plan2d;
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry plan3d;
}

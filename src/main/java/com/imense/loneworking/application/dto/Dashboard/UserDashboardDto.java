package com.imense.loneworking.application.dto.Dashboard;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Getter
@Setter
public class UserDashboardDto {
    private Long id;
    private String first_name;
    private String last_name;
    private String company_name;
    private Geometry position;
    private String status;
    private Long site_id;
    private String phone;
    private String zone;
    private String level;
    private String room;
}
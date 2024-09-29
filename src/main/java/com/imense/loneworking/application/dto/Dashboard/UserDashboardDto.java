package com.imense.loneworking.application.dto.Dashboard;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Getter
@Setter
@Data
public class UserDashboardDto {
    private Long id;
    private String first_name;
    private String last_name;
    private String profile_photo;
    private String company_name;
    private Long company_id;
    private Geometry position;
    private String status;
    private Long site_id;
    private String site_name;
    private String phone;
}
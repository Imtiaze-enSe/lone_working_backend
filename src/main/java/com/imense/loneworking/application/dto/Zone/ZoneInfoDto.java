package com.imense.loneworking.application.dto.Zone;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    private String siteName; // + site_id
    private String zoneName;
    private String companyName;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime zoneCreatedAt;
    private Geometry zonePlan;
}

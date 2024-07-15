package com.imense.loneworking.application.dto.Qrcode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class QrcodeAndZoneInfoDto {
    private Long zoneId;
    private String siteName;
    private String zoneName;
    private String level;
    private String room;
    private String interior;
    private Geometry geocoordinates;
}

package com.imense.loneworking.application.dto.Qrcode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class QrcodeCreationDto {
    private long siteId;
    private long zoneId;
    private String level;
    private String room;
    private String interior;
    private Geometry qr_code_position;
}

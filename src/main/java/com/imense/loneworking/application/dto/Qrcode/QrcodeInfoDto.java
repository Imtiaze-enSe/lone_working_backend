package com.imense.loneworking.application.dto.Qrcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
public class QrcodeInfoDto {
    private long id_qr_code;
    private String siteName;
    private String zoneName;
    private String level;
    private String room;
    private String interior;
    private Geometry geocoordinates;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;


}

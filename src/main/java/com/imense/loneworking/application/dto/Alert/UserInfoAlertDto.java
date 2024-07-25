package com.imense.loneworking.application.dto.Alert;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Data
@Getter
@Setter
public class UserInfoAlertDto {
    private Long user_id;
    private String name;
    private String phone;
    private Geometry position;
    private int duration;
    private String profile_photo;
}

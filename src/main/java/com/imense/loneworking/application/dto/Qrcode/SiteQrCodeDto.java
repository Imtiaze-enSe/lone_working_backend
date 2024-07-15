package com.imense.loneworking.application.dto.Qrcode;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SiteQrCodeDto {
    private long site_id;
    private String siteName;
}

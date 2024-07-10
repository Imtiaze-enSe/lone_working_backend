package com.imense.loneworking.application.service.serviceInterface;

import com.imense.loneworking.application.dto.Qrcode.QrcodeCreationDto;
import com.imense.loneworking.application.dto.Qrcode.QrcodeInfoDto;
import com.imense.loneworking.domain.entity.QrCode;

import java.util.List;

public interface QrcodeService {

    QrCode addQrcode(QrcodeCreationDto qrcodeCreationDto);

    List<QrcodeInfoDto> getAllQrCodes();
}

package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Qrcode.QrcodeCreationDto;
import com.imense.loneworking.application.dto.Qrcode.QrcodeInfoDto;
import com.imense.loneworking.application.service.serviceInterface.QrcodeService;
import com.imense.loneworking.domain.entity.QrCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QrCodeController {
    private final QrcodeService qrcodeService;

    public QrCodeController(QrcodeService qrcodeService) {
        this.qrcodeService = qrcodeService;
    }

    @PostMapping("web/qrCode")
    public QrCode addQrcode(@RequestBody QrcodeCreationDto qrcodeCreationDto){return qrcodeService.addQrcode(qrcodeCreationDto);}

    @GetMapping("web/qrCodes")
    public List<QrcodeInfoDto> getAllQrCodes() {
        return qrcodeService.getAllQrCodes();
    }

    @DeleteMapping("web/qrCode/{id}")
    public void deleteQrCode(@PathVariable Long id){qrcodeService.deleteQrCode(id);}

    @PutMapping("web/qrCode/{id}")
    public QrCode updateQrCode(@PathVariable Long id,@RequestBody QrcodeCreationDto qrcodeCreationDto){
        return qrcodeService.updateQrCode(id,qrcodeCreationDto);
    }


}

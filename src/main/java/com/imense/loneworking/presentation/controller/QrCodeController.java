package com.imense.loneworking.presentation.controller;

import com.imense.loneworking.application.dto.Qrcode.QrcodeAndZoneInfoDto;
import com.imense.loneworking.application.dto.Qrcode.QrcodeCreationDto;
import com.imense.loneworking.application.dto.Qrcode.QrcodeInfoDto;
import com.imense.loneworking.application.service.serviceInterface.QrcodeService;
import com.imense.loneworking.domain.entity.QrCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QrCodeController {
    private final QrcodeService qrcodeService;

    public QrCodeController(QrcodeService qrcodeService) {
        this.qrcodeService = qrcodeService;
    }

    // Add new QR Code
    @PostMapping("web/qrCode")
    public ResponseEntity<QrCode> addQrcode(@RequestBody QrcodeCreationDto qrcodeCreationDto) {
        try {
            QrCode qrCode = qrcodeService.addQrcode(qrcodeCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(qrCode);  // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all QR Codes
    @GetMapping("web/qrCodes")
    public ResponseEntity<List<QrcodeInfoDto>> getAllQrCodes() {
        try {
            List<QrcodeInfoDto> qrCodes = qrcodeService.getAllQrCodes();
            return ResponseEntity.ok(qrCodes);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete QR Code by ID
    @DeleteMapping("web/qrCode/{id}")
    public ResponseEntity<Void> deleteQrCode(@PathVariable Long id) {
        try {
            qrcodeService.deleteQrCode(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update QR Code by ID
    @PutMapping("web/qrCode/{id}")
    public ResponseEntity<QrCode> updateQrCode(@PathVariable Long id, @RequestBody QrcodeCreationDto qrcodeCreationDto) {
        try {
            QrCode qrCode = qrcodeService.updateQrCode(id, qrcodeCreationDto);
            return ResponseEntity.ok(qrCode);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get QR Code and Zone Info by ID
    @GetMapping("web/qrCode/qrAndZoneinfo/{id}")
    public ResponseEntity<QrcodeAndZoneInfoDto> getQrcodeAndZoneInfoDto(@PathVariable Long id) {
        try {
            QrcodeAndZoneInfoDto qrAndZoneInfo = qrcodeService.getQrcodeAndZoneInfoDto(id);
            return ResponseEntity.ok(qrAndZoneInfo);  // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

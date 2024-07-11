package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Qrcode.QrcodeAndZoneInfoDto;
import com.imense.loneworking.application.dto.Qrcode.QrcodeCreationDto;
import com.imense.loneworking.application.dto.Qrcode.QrcodeInfoDto;
import com.imense.loneworking.application.service.serviceInterface.QrcodeService;
import com.imense.loneworking.domain.entity.*;
import com.imense.loneworking.domain.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QrcodeServiceImpl implements QrcodeService {
    private final QrCodeRepository qrCodeRepository;


    private final ZoneRepository zoneRepository;

    private final UserRepository userRepository;


    public QrcodeServiceImpl(QrCodeRepository qrCodeRepository, ZoneRepository zoneRepository, UserRepository userRepository) {
        this.qrCodeRepository = qrCodeRepository;
        this.zoneRepository = zoneRepository;
        this.userRepository = userRepository;
    }

    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public QrCode addQrcode(QrcodeCreationDto qrcodeCreationDto) {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Tenant tenant = authUser.getTenant();
        List<Site> sites = tenant.getSites();

        long siteId = qrcodeCreationDto.getSiteId();
        Site site = sites.stream()
                .filter(s -> s.getId() == siteId)
                .findFirst()
                .orElse(null);
        if (site == null) {
            throw new RuntimeException("Site not found");
        }

        long zoneId = qrcodeCreationDto.getZoneId();
        List<Zone> zones = zoneRepository.findBySiteId(site.getId());
        Zone zone = zones.stream()
                .filter(z -> z.getId() == zoneId)
                .findFirst()
                .orElse(null);
        if (zone == null) {
            throw new RuntimeException("Zone not found");
        }

        QrCode qrCode = new QrCode();
        qrCode.setQr_code_position(qrcodeCreationDto.getQr_code_position());
        qrCode.setZone(zone);
        qrCode.setLevel(qrcodeCreationDto.getLevel());
        qrCode.setQr_code_position(qrCode.getQr_code_position());
        qrCode.setRoom(qrcodeCreationDto.getRoom());
        qrCode.setInterior(qrcodeCreationDto.getInterior());
        qrCode.setQr_code_createdAt(LocalDateTime.now());
        qrCode.setQr_code_upatedAt(LocalDateTime.now());
        return qrCodeRepository.save(qrCode);

    }

    @Override
    public List<QrcodeInfoDto> getAllQrCodes() {

        String username = getCurrentUsername();
        User user = userRepository.findByEmail(username);
        Tenant tenant = user.getTenant();
        List<Site> sites = tenant.getSites();

        List<QrcodeInfoDto> qrcodeInfoDtos = new ArrayList<>();
        for (Site site : sites) {
            List<Zone> zones = site.getZones();
            for (Zone zone : zones) {
                List<QrCode> qrCodes = zone.getQrCodes();

                for (QrCode qrCode : qrCodes) {
                    QrcodeInfoDto dto = new QrcodeInfoDto();
                    dto.setId_qr_code(qrCode.getId_qr_code());
                    dto.setSiteName(site.getName());
                    dto.setZoneName(zone.getName());
                    dto.setLevel(qrCode.getLevel());
                    dto.setRoom(qrCode.getRoom());
                    dto.setInterior(qrCode.getInterior());
                    dto.setGeocoordinates(qrCode.getQr_code_position());
                    dto.setCreatedAt(qrCode.getQr_code_createdAt());
                    qrcodeInfoDtos.add(dto);

                }


            }


        }
        return qrcodeInfoDtos;
    }

    @Override
    public void deleteQrCode(Long qrCodeId) {
        QrCode qrCode=qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new RuntimeException("QrCode not found"));
        qrCodeRepository.delete(qrCode);
    }

    @Override
    public QrCode updateQrCode(Long qrCodeId, QrcodeCreationDto qrcodeCreationDto) {

        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Tenant tenant = authUser.getTenant();
        List<Site> sites = tenant.getSites();

        QrCode qrCode=qrCodeRepository.findById(qrCodeId).orElseThrow(() -> new RuntimeException("QrCode not found"));
        long siteId = qrcodeCreationDto.getSiteId();
        Site site = sites.stream()
                .filter(s -> s.getId() == siteId)
                .findFirst()
                .orElse(null);
        if (site == null) {
            throw new RuntimeException("Site not found");
        }

        long zoneId = qrcodeCreationDto.getZoneId();
        List<Zone> zones = zoneRepository.findBySiteId(site.getId());
        Zone zone = zones.stream()
                .filter(z -> z.getId() == zoneId)
                .findFirst()
                .orElse(null);
        if (zone == null) {
            throw new RuntimeException("Zone not found");
        }

        qrCode.setZone(zone);
        qrCode.setInterior(qrcodeCreationDto.getInterior());
        qrCode.setLevel(qrcodeCreationDto.getLevel());
        qrCode.setRoom(qrcodeCreationDto.getRoom());
        qrCode.setQr_code_position(qrcodeCreationDto.getQr_code_position());
        qrCode.setQr_code_upatedAt(LocalDateTime.now());
        return qrCodeRepository.save(qrCode);

    }

    @Override
    public QrcodeAndZoneInfoDto getQrcodeAndZoneInfoDto(Long qrCodeId) {
        QrCode qrCode=qrCodeRepository.findById(qrCodeId).orElseThrow(() -> new RuntimeException("QrCode not found"));
        QrcodeAndZoneInfoDto qrcodeAndZoneInfoDto=new QrcodeAndZoneInfoDto();
        qrcodeAndZoneInfoDto.setGeocoordinates(qrCode.getQr_code_position());
        qrcodeAndZoneInfoDto.setZoneId(qrCode.getZone().getId());
        qrcodeAndZoneInfoDto.setRoom(qrCode.getRoom());
        qrcodeAndZoneInfoDto.setLevel(qrCode.getLevel());
        qrcodeAndZoneInfoDto.setInterior(qrCode.getInterior());
        return qrcodeAndZoneInfoDto;
    }
}




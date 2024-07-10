package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
}

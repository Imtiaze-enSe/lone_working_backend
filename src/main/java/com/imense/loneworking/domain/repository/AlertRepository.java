package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUserSiteId(Long siteId);

}

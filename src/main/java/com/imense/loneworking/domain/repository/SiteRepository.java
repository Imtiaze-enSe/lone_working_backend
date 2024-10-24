package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    Site findByName(String name);
    List<Site> findSitesByTenant_Id(Long tenant_id);
}

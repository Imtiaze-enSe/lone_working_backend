package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
    Site findByName(String name);
}

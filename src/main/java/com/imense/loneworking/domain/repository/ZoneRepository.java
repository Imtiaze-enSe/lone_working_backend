package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findBySiteId(Long siteId);
    Integer countZoneBySite(Optional<Site> site);

}

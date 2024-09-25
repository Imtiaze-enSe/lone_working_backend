package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.SiteSynchro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteSynchroRepository extends JpaRepository<SiteSynchro, Long> {
    Optional<SiteSynchro> findByRefId(Long ref_id);
    void deleteByRefId(Long refId);

}

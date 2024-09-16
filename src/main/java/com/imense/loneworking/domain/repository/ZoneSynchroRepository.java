package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.ZoneSynchro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneSynchroRepository extends JpaRepository<ZoneSynchro, Long> {
    Optional<ZoneSynchro> findByRefId(Long ref_id);
}

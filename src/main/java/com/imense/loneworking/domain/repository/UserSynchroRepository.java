package com.imense.loneworking.domain.repository;


import com.imense.loneworking.domain.entity.UserSynchro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSynchroRepository extends JpaRepository<UserSynchro, Long> {
    Optional<UserSynchro> findByRefId(Long ref_id);
}

package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findBySiteId(Long siteId);
    Boolean existsByEmail(String email);
    void deleteUsersBySiteId(Long site_id);
    List<User> findBySiteIdAndStatus(Long siteId, String status);
}

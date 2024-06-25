package com.imense.loneworking.domain.repository;

import com.imense.loneworking.domain.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByEmailClient(String emailClient);
}

package com.imense.loneworking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Update {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idUpdate;
    private String title;
    private String message;
    private Date updateCreatedAt;

    @OneToMany(mappedBy = "update")
    private Set<Alert> alerts;

}

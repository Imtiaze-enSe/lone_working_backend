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
@Table(name = "updates")
public class Update {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_update;
    private String title;
    private String message;
    private Date update_created_at;

    @ManyToOne
    @JoinColumn(name = "idAlert")
    private Alert alert;

}

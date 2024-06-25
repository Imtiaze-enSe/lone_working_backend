package com.imense.loneworking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idNote;
    private String note;
    private Date noteCreatedAt;

    @ManyToOne
    @JoinColumn(name = "idAlert")
    private Alert alert;

}

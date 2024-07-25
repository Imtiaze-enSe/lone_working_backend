package com.imense.loneworking.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_note;
    private String note;
    private LocalDateTime note_created_at;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idAlert")
    private Alert alert;


    @PrePersist
    protected void onCreate() {
        note_created_at = LocalDateTime.now();
    }

}

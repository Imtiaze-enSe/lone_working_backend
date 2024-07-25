package com.imense.loneworking.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private LocalDateTime update_created_at;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idAlert")
    private Alert alert;

    @PrePersist
    protected void onCreate() {
        update_created_at = LocalDateTime.now();
    }


}

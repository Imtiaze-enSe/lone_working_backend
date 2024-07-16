package com.imense.loneworking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_alert;
    private String alert_status;
    private String alert_type;
    private LocalDateTime alert_created_at;
    private LocalDateTime alert_updated_at;
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "alert")
    private Set<Update> updates;

    @PrePersist
    protected void onCreate() {
        alert_created_at = LocalDateTime.now();
        alert_updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        alert_updated_at = LocalDateTime.now();
    }


}

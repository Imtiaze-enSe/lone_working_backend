package com.imense.loneworking.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private int duration;
    private String zone;
    private String level;
    private String room;
    private String interior;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "alert")
    @JsonManagedReference
    private List<Update> updates;

    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Note> notes;

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

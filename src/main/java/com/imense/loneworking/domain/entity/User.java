package com.imense.loneworking.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.imense.loneworking.domain.entity.Enum.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.locationtech.jts.geom.Geometry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name;
    private String last_name;
    private String acronym;
    private String pin;
    @Column(name = "site_id")
    private Long siteId;
    private Date birthday;
    private Date login_at;
    private String phone;
    private String signature;
    @Lob
    private byte[] profile_photo;
    private String qr_password;
    private Boolean terms_accepted;
    private Long function_id;
    private String report_to;
    private String status;
    private Boolean ppe_status;
    private String email;
    private Date email_verified_at;
    private String password;
    private Boolean contact;
    private String remember_token;
    private String company_name;
    @Lob
    private String fcm_token;

    @Lob
    private byte[] company_logo;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    private Geometry position;

    private String address;
    private String contact_person;
    private String contact_person_phone;
    private String department;
    private String function;

    // health info
    private String drugs;
    private String blood_type;
    private String diseases;
    private String medications;
    private Boolean alcoholic;
    private Boolean smoking;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Alert> alerts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Notification> notifications;

    @Enumerated(EnumType.STRING)
    private UserRole role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    @PreRemove
    protected void onDelete() {
        deleted_at = LocalDateTime.now();
    }
}

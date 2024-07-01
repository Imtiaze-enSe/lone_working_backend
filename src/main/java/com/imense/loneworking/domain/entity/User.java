package com.imense.loneworking.domain.entity;

import com.imense.loneworking.domain.entity.Enum.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    private long site_id;
    private Date birthday;
    private Date login_at;
    private String phone;
    private String signature;
    private String profile_photo;
    private String qr_password;
    private Boolean terms_accepted;
    private Long function_id;
    private Long report_to;
    private Boolean status;
    private Boolean ppe_status;
    private String email;
    private Date email_verified_at;
    private String password;
    private Boolean contact;
    private String remember_token;
    @Lob
    private String fcm_token;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;

    private Geometry position;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @OneToMany(mappedBy = "user")
    private List<Alert> alerts;

    @OneToMany(mappedBy = "user")
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
}

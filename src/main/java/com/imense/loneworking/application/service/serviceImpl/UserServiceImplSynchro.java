package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.service.serviceInterface.UserServiceSynchro;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.SiteSynchroRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.domain.repository.UserSynchroRepository;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplSynchro implements UserServiceSynchro {
    private final UserSynchroRepository userRepository;
    private final SiteSynchroRepository siteRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory;

    public UserServiceImplSynchro(UserSynchroRepository userRepository, SiteSynchroRepository siteRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.passwordEncoder = passwordEncoder;
        this.geometryFactory = new GeometryFactory();
    }
}

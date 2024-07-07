package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.UserDashboardDto;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;

    public UserServiceImpl(UserRepository userRepository, SiteRepository siteRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
    }
    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public List<UserDashboardDto> getAllUsers() {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Long siteId = authUser.getSiteId();
        List<User> users = userRepository.findBySiteId(siteId);
        System.out.println(users);
        return users.stream().map(user -> {
            UserDashboardDto userDto = new UserDashboardDto();
            userDto.setId(user.getId());
            userDto.setFirst_name(user.getFirst_name());
            userDto.setLast_name(user.getLast_name());
            userDto.setCompany_name(user.getTenant().getName());
            userDto.setPosition(user.getPosition());
            userDto.setSite_id(user.getSiteId());
            userDto.setStatus(String.valueOf(user.getStatus()));
            userDto.setPhone(user.getPhone());
            userDto.setZone(user.getZone());
            userDto.setLevel(user.getLevel());
            userDto.setRoom(user.getRoom());
            return userDto;
        }).collect(Collectors.toList());
    }
}

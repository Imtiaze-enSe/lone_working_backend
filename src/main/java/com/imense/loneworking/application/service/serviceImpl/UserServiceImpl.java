package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.WorkerInfoDto;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<UserDashboardDto> getAllUsersForDashboard() {
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

    @Override
    public List<WorkerInfoDto> getAllUsersForTable() {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Long siteId = authUser.getSiteId();
        List<User> users = userRepository.findBySiteId(siteId);
        return users.stream().map(user -> {
            WorkerInfoDto workerDto = new WorkerInfoDto();
            workerDto.setId(user.getId());
            workerDto.setProfile_photo(user.getProfile_photo());
            workerDto.setFirst_name(user.getFirst_name());
            workerDto.setLast_name(user.getLast_name());
            workerDto.setPhone(user.getPhone());
            workerDto.setCompany(user.getTenant().getName());
            workerDto.setLinked_to(siteRepository.findById(user.getSiteId()).get().getName());
            workerDto.setCreated_at(user.getCreated_at());


            workerDto.setFunction(String.valueOf(user.getFunction_id()));
            workerDto.setAddress(user.getAddress());
            workerDto.setCompanyLogo(user.getTenant().getLogo());
            workerDto.setContact_person_phone(user.getContact_person_phone());
            workerDto.setContact_person(user.getContact_person());
            workerDto.setReport_to(String.valueOf(user.getReport_to()));


            workerDto.setAlcoholic(user.getAlcoholic());
            workerDto.setMedications(user.getMedications());
            workerDto.setBlood_type(user.getBlood_type());
            workerDto.setDiseases(user.getDiseases());
            workerDto.setSmoking(user.getSmoking());

            return workerDto;
        }).collect(Collectors.toList());
    }
}

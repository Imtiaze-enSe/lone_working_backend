package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.Zone;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.TenantRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.imense.loneworking.domain.entity.Enum.UserRole.WORKER;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, SiteRepository siteRepository,
                           TenantRepository tenantRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
    }
    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public List<UserDashboardDto> getAllUsersForDashboard(Long site_id) {
        List<User> users = userRepository.findBySiteId(site_id);
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
            if (user.getProfile_photo() != null) {
                userDto.setProfile_photo(Base64.getEncoder().encodeToString(user.getProfile_photo()));
            } else {
                user.setProfile_photo(null);
            }
            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<WorkerInfoDto> getAllUsersForTable() {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Tenant tenant = authUser.getTenant();
        List<User> users = tenant.getUsers();

        return users.stream().map(user -> {
            WorkerInfoDto workerDto = new WorkerInfoDto();
            workerDto.setId(user.getId());

            // Check for null profile photo before encoding
            if (user.getProfile_photo() != null) {
                workerDto.setProfile_photo(Base64.getEncoder().encodeToString(user.getProfile_photo()));
            } else {
                workerDto.setProfile_photo(null);
            }

            workerDto.setFirst_name(user.getFirst_name());
            workerDto.setLast_name(user.getLast_name());
            workerDto.setEmail(user.getEmail());
            workerDto.setPhone(user.getPhone());

            // Check for null company logo before encoding
            if (user.getCompany_logo() != null) {
                workerDto.setCompany_logo(Base64.getEncoder().encodeToString(user.getCompany_logo()));
            } else {
                workerDto.setCompany_logo(null);
            }

            workerDto.setCreated_at(user.getCreated_at());

            workerDto.setFunction(user.getFunction());
            // Check if site exists before accessing the name
            siteRepository.findById(user.getSiteId()).ifPresentOrElse(
                    site -> workerDto.setSite_name(site.getName()),
                    () -> workerDto.setSite_name(null) // or handle the absence as appropriate
            );
            workerDto.setAddress(user.getAddress());
            workerDto.setContact_person_phone(user.getContact_person_phone());
            workerDto.setContact_person(user.getContact_person());
            workerDto.setReport_to(user.getReport_to());
            workerDto.setDepartment(user.getDepartment());

            workerDto.setAlcoholic(user.getAlcoholic());
            workerDto.setMedications(user.getMedications());
            workerDto.setBlood_type(user.getBlood_type());
            workerDto.setDiseases(user.getDiseases());
            workerDto.setSmoking(user.getSmoking());

            return workerDto;
        }).collect(Collectors.toList());
    }


    @Override
    public User addWorker(WorkerCreationDto workerCreationDto) {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        Long siteId = authUser.getSiteId();
        Optional<Site> thisSite = siteRepository.findById(siteId);
        Tenant tenant =  thisSite.get().getTenant();
        System.out.println(tenant);

        // Check if the email already exists in the database
        if (userRepository.existsByEmail(workerCreationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Site site = siteRepository.findByName(workerCreationDto.getSite_name());
        System.out.println(tenant);
        if (site == null) {
            throw new RuntimeException("Site not found");
        }

        User user = new User();

        if (workerCreationDto.getProfile_photo() != null) {
            user.setProfile_photo(Base64.getDecoder().decode(workerCreationDto.getProfile_photo()));
        } else {
            user.setProfile_photo(null); // or handle as needed
        }
        if (workerCreationDto.getCompany_logo() != null) {
            user.setCompany_logo(Base64.getDecoder().decode(workerCreationDto.getCompany_logo()));
        } else {
            user.setCompany_logo(null); // or handle as needed
        }

        user.setSiteId(site.getId());
        user.setFirst_name(workerCreationDto.getFirst_name());
        user.setLast_name(workerCreationDto.getLast_name());
        user.setEmail(workerCreationDto.getEmail());
        user.setPassword(passwordEncoder.encode(workerCreationDto.getPassword()));
        user.setPhone(workerCreationDto.getPhone());
        user.setTenant(tenant);
        user.setDepartment(workerCreationDto.getDepartment());
        user.setFunction(workerCreationDto.getFunction());
        user.setReport_to(workerCreationDto.getReport_to());

        user.setRole(WORKER);

        return userRepository.save(user);
    }

    @Override
    public User updateWorker(Long id, WorkerCreationDto workerCreationDto) {
        Site site = siteRepository.findByName(workerCreationDto.getSite_name());
        if (site == null) {
            throw new RuntimeException("Site not found");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (workerCreationDto.getProfile_photo() != null) {
            user.setProfile_photo(Base64.getDecoder().decode(workerCreationDto.getProfile_photo()));
        } else {
            user.setProfile_photo(null); // or handle as needed
        }
        if (workerCreationDto.getCompany_logo() != null) {
            user.setCompany_logo(Base64.getDecoder().decode(workerCreationDto.getCompany_logo()));
        } else {
            user.setCompany_logo(null); // or handle as needed
        }
        user.setFirst_name(workerCreationDto.getFirst_name());
        user.setLast_name(workerCreationDto.getLast_name());
        user.setEmail(workerCreationDto.getEmail());
        user.setPassword(passwordEncoder.encode(workerCreationDto.getPassword()));
        user.setPhone(workerCreationDto.getPhone());
        user.setDepartment(workerCreationDto.getDepartment());
        user.setFunction(workerCreationDto.getFunction());

        user.setRole(WORKER);

        return userRepository.save(user);
    }

    @Override
    public void deleteWorker(Long workerId) {
        User user = userRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }


    @Override
    public AuthenticatedUserDto getAuthenticatedUser() {
        String username = getCurrentUsername();
        System.out.println("Helloooo : " + username);
        User authUser = userRepository.findByEmail(username);
        AuthenticatedUserDto authenticatedUserDto=new AuthenticatedUserDto();
        authenticatedUserDto.setId(authUser.getId());

        if (authUser.getProfile_photo() != null) {
            authenticatedUserDto.setProfile_photo(Base64.getEncoder().encodeToString(authUser.getProfile_photo()));
        } else {
            authenticatedUserDto.setProfile_photo(null);
        }

        if (authUser.getCompany_logo() != null) {
            authenticatedUserDto.setCompany_logo(Base64.getEncoder().encodeToString(authUser.getCompany_logo()));
        } else {
            authenticatedUserDto.setCompany_logo(null);
        }
        authenticatedUserDto.setFirst_name(authUser.getFirst_name());
        authenticatedUserDto.setLast_name(authUser.getLast_name());
        authenticatedUserDto.setEmail(authUser.getEmail());
        authenticatedUserDto.setPhone(authUser.getPhone());
        authenticatedUserDto.setFunction(authUser.getFunction());
        authenticatedUserDto.setAddress(authUser.getAddress());
        authenticatedUserDto.setSite_id(authUser.getSiteId());
        return authenticatedUserDto;

    }

    @Override
    public User editProfileUser(EditProfileUserDto editProfileUserDto) {
        User user = userRepository.findById(editProfileUserDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirst_name(editProfileUserDto.getFirst_name());
        user.setLast_name(editProfileUserDto.getLast_name());
        user.setEmail(editProfileUserDto.getEmail());
        user.setPhone(editProfileUserDto.getPhone());
        user.setFunction(editProfileUserDto.getFunction());
        user.setAddress(editProfileUserDto.getAddress());
        if (editProfileUserDto.getProfile_photo() != null) {
            user.setProfile_photo(Base64.getDecoder().decode(editProfileUserDto.getProfile_photo()));
        } else {
            user.setProfile_photo(null); // or handle as needed
        }
        return userRepository.save(user);
    }


    @Override
    public User changePasswordUser(ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(changePasswordDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        return userRepository.save(user);
    }

}

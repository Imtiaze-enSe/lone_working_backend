package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.TenantRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory;

    public UserServiceImpl(UserRepository userRepository, SiteRepository siteRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.passwordEncoder = passwordEncoder;
        this.geometryFactory = new GeometryFactory();
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
            userDto.setEmail(user.getEmail());
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
        user.setStatus("Disconnected");
        user.setDepartment(workerCreationDto.getDepartment());
        user.setFunction(workerCreationDto.getFunction());
        user.setReport_to(workerCreationDto.getReport_to());

        user.setRole(WORKER);

        return userRepository.save(user);
    }

    @Override
    public User updateWorker(Long id, WorkerCreationDto workerCreationDto) {
        Site site = siteRepository.findByName(workerCreationDto.getSite_name()); // Use the correct repository query
        if (site == null) {
            throw new RuntimeException("Site not found");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setSiteId(site.getId());         // Set the site ID
        user.setSite_name(site.getName());    // Use the `name` field from `Site`

        // Update other user details...
        user.setFirst_name(workerCreationDto.getFirst_name());
        user.setLast_name(workerCreationDto.getLast_name());
        user.setEmail(workerCreationDto.getEmail());
        user.setPhone(workerCreationDto.getPhone());
        user.setDepartment(workerCreationDto.getDepartment());
        user.setFunction(workerCreationDto.getFunction());

        if (workerCreationDto.getProfile_photo() != null) {
            user.setProfile_photo(Base64.getDecoder().decode(workerCreationDto.getProfile_photo()));
        }
        if (workerCreationDto.getCompany_logo() != null) {
            user.setCompany_logo(Base64.getDecoder().decode(workerCreationDto.getCompany_logo()));
        }

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
        authenticatedUserDto.setCompany_name(authUser.getTenant().getName());
        authenticatedUserDto.setCompany_email(authUser.getTenant().getEmail());
        authenticatedUserDto.setCompany_phone(authUser.getTenant().getPhone());
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
    public EditProfileMobileDto getUserForMobileSettings() {
        // Get the current authenticated username
        String username = getCurrentUsername();

        // Find the user by their email (which is the username)
        User authUser = userRepository.findByEmail(username);

        // Create a new EditProfileMobileDto and populate it with the user's current details
        EditProfileMobileDto editProfileMobileDto = new EditProfileMobileDto();
        editProfileMobileDto.setId(authUser.getId());
        if (authUser.getProfile_photo() != null) {
            editProfileMobileDto.setProfile_photo(Base64.getEncoder().encodeToString(authUser.getProfile_photo()));
        } else {
            editProfileMobileDto.setProfile_photo(null);
        }
        if (authUser.getCompany_logo() != null) {
            editProfileMobileDto.setCompany_logo(Base64.getEncoder().encodeToString(authUser.getCompany_logo()));
        } else {
            editProfileMobileDto.setCompany_logo(null);
        }
        editProfileMobileDto.setFirst_name(authUser.getFirst_name());
        editProfileMobileDto.setLast_name(authUser.getLast_name());
        editProfileMobileDto.setEmail(authUser.getEmail());
        editProfileMobileDto.setPhone(authUser.getPhone());
        editProfileMobileDto.setFunction(authUser.getFunction());
        editProfileMobileDto.setAddress(authUser.getAddress());
        //editProfileMobileDto.setPassword(authUser.getPassword());
        editProfileMobileDto.setContact_person(authUser.getContact_person());
        editProfileMobileDto.setContact_person_phone(authUser.getContact_person_phone());
        editProfileMobileDto.setReport_to(authUser.getReport_to());
        editProfileMobileDto.setCompany_name(authUser.getTenant().getName());
        editProfileMobileDto.setBlood_type(authUser.getBlood_type());
        editProfileMobileDto.setDiseases(authUser.getDiseases());
        editProfileMobileDto.setMedications(authUser.getMedications());
        editProfileMobileDto.setAlcoholic(authUser.getAlcoholic());
        editProfileMobileDto.setSmoking(authUser.getSmoking());
        editProfileMobileDto.setPin(authUser.getPin());
        editProfileMobileDto.setCompany_name(authUser.getCompany_name());
        editProfileMobileDto.setDrugs(authUser.getDrugs());
        editProfileMobileDto.setDiseases(authUser.getDiseases());

        // Return the populated EditProfileMobileDto
        return editProfileMobileDto;
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
    @Override
    public User settingsMobile(EditProfileMobileDto editProfileMobileDto) {
        User user = userRepository.findById(editProfileMobileDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional.ofNullable(editProfileMobileDto.getFirst_name()).ifPresent(user::setFirst_name);
        Optional.ofNullable(editProfileMobileDto.getLast_name()).ifPresent(user::setLast_name);
        Optional.ofNullable(editProfileMobileDto.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(editProfileMobileDto.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(editProfileMobileDto.getFunction()).ifPresent(user::setFunction);
        Optional.ofNullable(editProfileMobileDto.getAddress()).ifPresent(user::setAddress);

        Optional.ofNullable(editProfileMobileDto.getProfile_photo())
                .ifPresent(photo -> user.setProfile_photo(Base64.getDecoder().decode(photo)));

        Optional.ofNullable(editProfileMobileDto.getCompany_logo())
                .ifPresent(photo -> user.setCompany_logo(Base64.getDecoder().decode(photo)));

        Optional.ofNullable(editProfileMobileDto.getPassword())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));

        Optional.ofNullable(editProfileMobileDto.getContact_person()).ifPresent(user::setContact_person);
        Optional.ofNullable(editProfileMobileDto.getContact_person_phone()).ifPresent(user::setContact_person_phone);
        Optional.ofNullable(editProfileMobileDto.getReport_to()).ifPresent(user::setReport_to);
        Optional.ofNullable(editProfileMobileDto.getCompany_name()).ifPresent(user::setCompany_name);
        Optional.ofNullable(editProfileMobileDto.getSmoking()).ifPresent(user::setSmoking);
        Optional.ofNullable(editProfileMobileDto.getAlcoholic()).ifPresent(user::setAlcoholic);
        Optional.ofNullable(editProfileMobileDto.getMedications()).ifPresent(user::setMedications);
        Optional.ofNullable(editProfileMobileDto.getDrugs()).ifPresent(user::setDrugs);
        Optional.ofNullable(editProfileMobileDto.getDiseases()).ifPresent(user::setDiseases);
        Optional.ofNullable(editProfileMobileDto.getPin()).ifPresent(user::setPin);
        Optional.ofNullable(editProfileMobileDto.getBlood_type()).ifPresent(user::setBlood_type);

        return userRepository.save(user);
    }

    @Override
    public PinSettingsDto getPinSettings() {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        PinSettingsDto pinSettingsDto = new PinSettingsDto();
        pinSettingsDto.setPin(authUser.getPin());
        return pinSettingsDto;
    }

    @Override
    public User updateUserPin(PinSettingsDto pinSettingsDto) {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        authUser.setPin(pinSettingsDto.getPin());
        return userRepository.save(authUser);
    }

    @Override
    public UserTermsDto getUserTerms() {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        UserTermsDto userTermsDto = new UserTermsDto();
        if(authUser.getTerms_accepted() != null) {
            userTermsDto.setTerms_accepted(authUser.getTerms_accepted());
        }
        else {
        userTermsDto.setTerms_accepted(false);}

        return userTermsDto;
    }

    @Override
    public User updateUserTerms(UserTermsDto userTermsDto) {
        String username = getCurrentUsername();
        User authUser = userRepository.findByEmail(username);
        authUser.setTerms_accepted(userTermsDto.getTerms_accepted());
        return userRepository.save(authUser);
    }

    @Override
    public boolean isUserInSite(Long userId, Double longitude, Double latitude) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        Long siteId = user.getSiteId();
        // Find the site
        Optional<Site> siteOptional = siteRepository.findById(siteId);
        if (siteOptional.isEmpty()) {
            return false;
        }
        Site site = siteOptional.get();
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        // Check if the user's location is inside the site's plan
        return site.getPlan2d() != null && site.getPlan2d().covers(point);
    }
}

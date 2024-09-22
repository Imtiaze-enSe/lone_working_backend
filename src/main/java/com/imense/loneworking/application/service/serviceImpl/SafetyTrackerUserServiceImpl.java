package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.application.service.serviceInterface.SafetyTrackerUserService;
import com.imense.loneworking.application.service.serviceInterface.UserService;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.Tenant;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.UserSynchro;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.TenantRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.domain.repository.UserSynchroRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.imense.loneworking.domain.entity.Enum.UserRole.WORKER;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class SafetyTrackerUserServiceImpl implements SafetyTrackerUserService {
    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory;
    private final WebClient webClient;
    private final UserSynchroRepository userSynchroRepository;

    public SafetyTrackerUserServiceImpl(UserRepository userRepository, SiteRepository siteRepository, PasswordEncoder passwordEncoder,WebClient.Builder webClientBuilder,UserSynchroRepository userSynchroRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.passwordEncoder = passwordEncoder;
        this.geometryFactory = new GeometryFactory();
        this.webClient = webClientBuilder.baseUrl("https://core-dev.safetytracker.ma/api/v1").build();
        this.userSynchroRepository =userSynchroRepository;
    }
    private String getCurrentUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public List<UserDashboardDto> getAllUsersForDashboard(Long site_id) {
        // here i should add the tenant_id to the frontend
        // here in this API sometimes the site is null whyy?
        int tenant_id=1;

        List<UserDashboardDto> users = new ArrayList<>();
        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";
        try
        {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loneworking/users/{tenant_id}")
                            .build(tenant_id))  // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token)  // Use existing token
                    .retrieve()
                    .bodyToMono(String.class)  // Get the response as a String
                    .block();
System.out.println(response);
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                // Parse the response into a JSON tree
                JsonNode root = objectMapper.readTree(response);

                // Extract the 'data' array
                JsonNode dataArray = root.get("data");

                if (dataArray != null && dataArray.isArray()) {
                    for (JsonNode item : dataArray) {
                        // Check if the site's id matches your site_id
                        // Check if 'site' exists and is not null
                        JsonNode siteNode = item.get("site");
                        if (siteNode != null) {
                            // Check if 'id' exists in 'site'
                            JsonNode siteIdNode = siteNode.get("id");
                            if (siteIdNode != null) {
                                // Check if the site's id matches your site_id
                                String siteId = siteIdNode.asText();

                                if (siteId.equals(site_id.toString())) {
                                                UserDashboardDto userDto = new UserDashboardDto();
                                                Long user_id=item.get("id").asLong();
                                    Optional<UserSynchro> userSynchro = userSynchroRepository.findById(user_id);
if(userSynchro.isPresent()) {
    userDto.setId(user_id);
    userDto.setFirst_name(item.get("first_name").asText());
    userDto.setLast_name(item.get("last_name").asText());
    userDto.setCompany_name(item.get("tenant").get("name").asText());
    userDto.setPosition(userSynchro.get().getPosition());
    userDto.setSite_id(site_id);
    userDto.setStatus(userSynchro.get().getStatus());
    userDto.setPhone(item.get("phone").asText());
//            if (item.get("profile_photo").asText() != null) {
//                userDto.setProfile_photo(Base64.getEncoder().encodeToString(item.get("profile_photo").asText()));
//            } else {
//                userDto.setProfile_photo(null);
//            }
    users.add(userDto);
}
                                }
                            } else {
                                System.out.println("Field 'id' is missing in 'site' object.");
                            }
                        } else {
                            System.out.println("Field 'site' is missing or null.");
                        }

                    }
                } else {
                    System.out.println("No data array found in the response.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return users;

    }

//    @Override
//    public List<WorkerInfoDto> getAllUsersForTable() {
//        // i should add the tenant id in the frontend
//        int tenant_id=1;
//
//        List<WorkerInfoDto> users = new ArrayList<>();
//        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";
//        try
//        {
//            String response = webClient.get()
//                    .uri(uriBuilder -> uriBuilder
//                            .path("/loneworking/users/{tenant_id}")
//                            .build(tenant_id))  // Build the URI with dynamic site ID
//                    .header("Accept", "application/vnd.api+json")
//                    .header("Authorization", "Bearer " + token)  // Use existing token
//                    .retrieve()
//                    .bodyToMono(String.class)  // Get the response as a String
//                    .block();
//            System.out.println(response);
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            try {
//                // Parse the response into a JSON tree
//                JsonNode root = objectMapper.readTree(response);
//
//                // Extract the 'data' array
//                JsonNode dataArray = root.get("data");
//
//                if (dataArray != null && dataArray.isArray()) {
//                    for (JsonNode item : dataArray) {
//                        WorkerInfoDto workerDto = new WorkerInfoDto();
//                                    Long user_id=item.get("id").asLong();
//                                    Optional<UserSynchro> userSynchro = userSynchroRepository.findById(user_id);
//                                    if(userSynchro.isPresent()) {
//                                        workerDto.setId(user_id);
//
//                                        // Check for null profile photo before encoding
////                                        if (user.getProfile_photo() != null) {
////                                            workerDto.setProfile_photo(Base64.getEncoder().encodeToString(user.getProfile_photo()));
////                                        } else {
////                                            workerDto.setProfile_photo(null);
////                                        }
//
//                                        workerDto.setFirst_name(item.get("first_name").asText());
//                                        workerDto.setLast_name(item.get("last_name").asText());
//                                        workerDto.setEmail(item.get("email").asText());
//                                        workerDto.setPhone(item.get("phone").asText());
//
//                                        // Check for null company logo before encoding
////                                        if (user.getCompany_logo() != null) {
////                                            workerDto.setCompany_logo(Base64.getEncoder().encodeToString(user.getCompany_logo()));
////                                        } else {
////                                            workerDto.setCompany_logo(null);
////                                        }
//
//                                        //workerDto.setCreated_at(item.get("created_at").asText());
//
//                                        workerDto.setFunction(item.get("function").asText());
//                                        workerDto.setSite_name(item.get("site").get("name").asText());
//                                        workerDto.setAddress(item.get("address").asText());
//                                        //workerDto.setContact_person_phone(user.getContact_person_phone());
//                                        //workerDto.setContact_person(user.getContact_person());
//                                        workerDto.setReport_to(item.get("report_to").get("first_name").asText()+" "+item.get("report_to").get("last_name").asText());
//                                        //workerDto.setDepartment(user.getDepartment());
//
//                                        workerDto.setAlcoholic(userSynchro.get().getAlcoholic());
//                                        workerDto.setMedications(userSynchro.get().getMedications());
//                                        workerDto.setBlood_type(userSynchro.get().getBlood_type());
//                                        workerDto.setDiseases(userSynchro.get().getDiseases());
//                                        workerDto.setSmoking(userSynchro.get().getSmoking());
//
//                                        users.add(workerDto);
//
//                        }
//
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return users;
//
//    }



    @Override
    public List<WorkerInfoDto> getAllUsersForTable() {
        // I should add the tenant id in the frontend
        int tenant_id = 1;

        List<WorkerInfoDto> users = new ArrayList<>();
        String token = "1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loneworking/users/{tenant_id}")
                            .build(tenant_id))  // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token)  // Use existing token
                    .retrieve()
                    .bodyToMono(String.class)  // Get the response as a String
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            JsonNode dataArray = root.get("data");

            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode item : dataArray) {
                    WorkerInfoDto workerDto = new WorkerInfoDto();
                    Long user_id = item.get("id").asLong();
                    Optional<UserSynchro> userSynchro = userSynchroRepository.findById(user_id);

                    if (userSynchro.isPresent()) {
                        workerDto.setId(user_id);

                        // Check for null profile photo before encoding
//                    if (user.getProfile_photo() != null) {
//                        workerDto.setProfile_photo(Base64.getEncoder().encodeToString(user.getProfile_photo()));
//                    } else {
//                        workerDto.setProfile_photo(null);
//                    }

                        workerDto.setFirst_name(getJsonValue(item, "first_name"));
                        workerDto.setLast_name(getJsonValue(item, "last_name"));
                        workerDto.setEmail(getJsonValue(item, "email"));
                        workerDto.setPhone(getJsonValue(item, "phone"));

                        // Check for null company logo before encoding
//                    if (user.getCompany_logo() != null) {
//                        workerDto.setCompany_logo(Base64.getEncoder().encodeToString(user.getCompany_logo()));
//                    } else {
//                        workerDto.setCompany_logo(null);
//                    }

                        //workerDto.setCreated_at(item.get("created_at").asText());

                        workerDto.setFunction(getJsonValue(item, "function"));

                        // Handle nested 'site' object safely
                        JsonNode siteNode = item.get("site");
                        if (siteNode != null && siteNode.has("name")) {
                            workerDto.setSite_name(siteNode.get("name").asText());
                        } else {
                            workerDto.setSite_name(null);  // or some default value
                        }

                        workerDto.setAddress(getJsonValue(item, "address"));

                        // Handle nested 'report_to' object safely
                        JsonNode reportToNode = item.get("report_to");
                        if (reportToNode != null && reportToNode.has("first_name") && reportToNode.has("last_name")) {
                            String reportToName = reportToNode.get("first_name").asText() + " " + reportToNode.get("last_name").asText();
                            workerDto.setReport_to(reportToName);
                        } else {
                            workerDto.setReport_to(null);  // or some default value
                        }

                        //workerDto.setDepartment(user.getDepartment());

                        // Additional attributes from userSynchro
                        workerDto.setAlcoholic(userSynchro.get().getAlcoholic());
                        workerDto.setMedications(userSynchro.get().getMedications());
                        workerDto.setBlood_type(userSynchro.get().getBlood_type());
                        workerDto.setDiseases(userSynchro.get().getDiseases());
                        workerDto.setSmoking(userSynchro.get().getSmoking());

                        users.add(workerDto);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // Helper function to handle null values
    private String getJsonValue(JsonNode node, String fieldName) {
        return node != null && node.has(fieldName) ? node.get(fieldName).asText() : null;
    }



//    @Override
//    public User addWorker(WorkerCreationDto workerCreationDto) {
//        String username = getCurrentUsername();
//        User authUser = userRepository.findByEmail(username);
//        Long siteId = authUser.getSiteId();
//        Optional<Site> thisSite = siteRepository.findById(siteId);
//        Tenant tenant =  thisSite.get().getTenant();
//        System.out.println(tenant);
//
//        // Check if the email already exists in the database
//        if (userRepository.existsByEmail(workerCreationDto.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        Site site = siteRepository.findByName(workerCreationDto.getSite_name());
//        System.out.println(tenant);
//        if (site == null) {
//            throw new RuntimeException("Site not found");
//        }
//
//        User user = new User();
//
//        if (workerCreationDto.getProfile_photo() != null) {
//            user.setProfile_photo(Base64.getDecoder().decode(workerCreationDto.getProfile_photo()));
//        } else {
//            user.setProfile_photo(null); // or handle as needed
//        }
//        if (workerCreationDto.getCompany_logo() != null) {
//            user.setCompany_logo(Base64.getDecoder().decode(workerCreationDto.getCompany_logo()));
//        } else {
//            user.setCompany_logo(null); // or handle as needed
//        }
//
//        user.setSiteId(site.getId());
//        user.setFirst_name(workerCreationDto.getFirst_name());
//        user.setLast_name(workerCreationDto.getLast_name());
//        user.setEmail(workerCreationDto.getEmail());
//        user.setPassword(passwordEncoder.encode(workerCreationDto.getPassword()));
//        user.setPhone(workerCreationDto.getPhone());
//        user.setTenant(tenant);
//        user.setStatus("Disconnected");
//        user.setDepartment(workerCreationDto.getDepartment());
//        user.setFunction(workerCreationDto.getFunction());
//        user.setReport_to(workerCreationDto.getReport_to());
//
//        user.setRole(WORKER);
//
//        return userRepository.save(user);
//    }
//
//    @Override
//    public User updateWorker(Long id, WorkerCreationDto workerCreationDto) {
//        Site site = siteRepository.findByName(workerCreationDto.getSite_name());
//        if (site == null) {
//            throw new RuntimeException("Site not found");
//        }
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (workerCreationDto.getProfile_photo() != null) {
//            user.setProfile_photo(Base64.getDecoder().decode(workerCreationDto.getProfile_photo()));
//        } else {
//            user.setProfile_photo(null); // or handle as needed
//        }
//        if (workerCreationDto.getCompany_logo() != null) {
//            user.setCompany_logo(Base64.getDecoder().decode(workerCreationDto.getCompany_logo()));
//        } else {
//            user.setCompany_logo(null); // or handle as needed
//        }
//        user.setFirst_name(workerCreationDto.getFirst_name());
//        user.setLast_name(workerCreationDto.getLast_name());
//        user.setEmail(workerCreationDto.getEmail());
//        user.setPassword(passwordEncoder.encode(workerCreationDto.getPassword()));
//        user.setPhone(workerCreationDto.getPhone());
//        user.setDepartment(workerCreationDto.getDepartment());
//        user.setFunction(workerCreationDto.getFunction());
//
//
//        return userRepository.save(user);
//    }
//
//    @Override
//    public void deleteWorker(Long workerId) {
//        User user = userRepository.findById(workerId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        userRepository.delete(user);
//    }
//

    @Override
    public AuthenticatedUserDto getAuthenticatedUser() {
        //user_id as variable
        Long user_id= 2L;
        AuthenticatedUserDto authenticatedUserDto=new AuthenticatedUserDto();
        authenticatedUserDto.setId(user_id);
        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";
        try
        {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loneworking/users/{user_id}")
                            .build(user_id))  // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token)  // Use existing token
                    .retrieve()
                    .bodyToMono(String.class)  // Get the response as a String
                    .block();
            System.out.println(response);
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode root = objectMapper.readTree(response);
            JsonNode dataArray = root.get("data");

            if (dataArray != null && dataArray.isArray()) {
                for (JsonNode item : dataArray) {



//        if (authUser.getProfile_photo() != null) {
//            authenticatedUserDto.setProfile_photo(Base64.getEncoder().encodeToString(authUser.getProfile_photo()));
//        } else {
//            authenticatedUserDto.setProfile_photo(null);
//        }
//
//        if (authUser.getCompany_logo() != null) {
//            authenticatedUserDto.setCompany_logo(Base64.getEncoder().encodeToString(authUser.getCompany_logo()));
//        } else {
//            authenticatedUserDto.setCompany_logo(null);
//        }
                    authenticatedUserDto.setFirst_name(item.get("first_name").asText());
                    authenticatedUserDto.setLast_name(item.get("last_name").asText());
                    authenticatedUserDto.setEmail(item.get("email").asText());
                    authenticatedUserDto.setPhone(item.get("phone").asText());
                    authenticatedUserDto.setFunction(item.get("function").get("name").asText());
                    authenticatedUserDto.setAddress(item.get("address").asText());
                    authenticatedUserDto.setSite_id(item.get("site").get("id").asLong());
                    authenticatedUserDto.setCompany_name(item.get("tenant").get("name").asText());
//                    authenticatedUserDto.setCompany_email(authUser.getTenant().getEmail());
//                    authenticatedUserDto.setCompany_phone(authUser.getTenant().getPhone());

                }
            }




        }
        catch (Exception e){
            e.printStackTrace();
        }
        return authenticatedUserDto;
    }

//    @Override
//    public User editProfileUser(EditProfileUserDto editProfileUserDto) {
//        User user = userRepository.findById(editProfileUserDto.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        user.setFirst_name(editProfileUserDto.getFirst_name());
//        user.setLast_name(editProfileUserDto.getLast_name());
//        user.setEmail(editProfileUserDto.getEmail());
//        user.setPhone(editProfileUserDto.getPhone());
//        user.setFunction(editProfileUserDto.getFunction());
//        user.setAddress(editProfileUserDto.getAddress());
//        if (editProfileUserDto.getProfile_photo() != null) {
//            user.setProfile_photo(Base64.getDecoder().decode(editProfileUserDto.getProfile_photo()));
//        } else {
//            user.setProfile_photo(null); // or handle as needed
//        }
//        return userRepository.save(user);
//    }
//
//    @Override
//    public EditProfileMobileDto getUserForMobileSettings() {
//        // Get the current authenticated username
//        String username = getCurrentUsername();
//
//        // Find the user by their email (which is the username)
//        User authUser = userRepository.findByEmail(username);
//
//        // Create a new EditProfileMobileDto and populate it with the user's current details
//        EditProfileMobileDto editProfileMobileDto = new EditProfileMobileDto();
//        editProfileMobileDto.setId(authUser.getId());
//        if (authUser.getProfile_photo() != null) {
//            editProfileMobileDto.setProfile_photo(Base64.getEncoder().encodeToString(authUser.getProfile_photo()));
//        } else {
//            editProfileMobileDto.setProfile_photo(null);
//        }
//        if (authUser.getCompany_logo() != null) {
//            editProfileMobileDto.setCompany_logo(Base64.getEncoder().encodeToString(authUser.getCompany_logo()));
//        } else {
//            editProfileMobileDto.setCompany_logo(null);
//        }
//        editProfileMobileDto.setFirst_name(authUser.getFirst_name());
//        editProfileMobileDto.setLast_name(authUser.getLast_name());
//        editProfileMobileDto.setEmail(authUser.getEmail());
//        editProfileMobileDto.setPhone(authUser.getPhone());
//        editProfileMobileDto.setFunction(authUser.getFunction());
//        editProfileMobileDto.setAddress(authUser.getAddress());
//        //editProfileMobileDto.setPassword(authUser.getPassword());
//        editProfileMobileDto.setContact_person(authUser.getContact_person());
//        editProfileMobileDto.setContact_person_phone(authUser.getContact_person_phone());
//        editProfileMobileDto.setReport_to(authUser.getReport_to());
//        editProfileMobileDto.setCompany_name(authUser.getTenant().getName());
//        editProfileMobileDto.setBlood_type(authUser.getBlood_type());
//        editProfileMobileDto.setDiseases(authUser.getDiseases());
//        editProfileMobileDto.setMedications(authUser.getMedications());
//        editProfileMobileDto.setAlcoholic(authUser.getAlcoholic());
//        editProfileMobileDto.setSmoking(authUser.getSmoking());
//        editProfileMobileDto.setPin(authUser.getPin());
//        editProfileMobileDto.setCompany_name(authUser.getCompany_name());
//        editProfileMobileDto.setDrugs(authUser.getDrugs());
//        editProfileMobileDto.setDiseases(authUser.getDiseases());
//
//        // Return the populated EditProfileMobileDto
//        return editProfileMobileDto;
//    }
//
//
//    @Override
//    public User changePasswordUser(ChangePasswordDto changePasswordDto) {
//        User user = userRepository.findById(changePasswordDto.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
//            throw new RuntimeException("Current password is incorrect");
//        }
//
//        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
//            throw new RuntimeException("New password and confirmation do not match");
//        }
//
//        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
//
//        return userRepository.save(user);
//    }
//    @Override
//    public User settingsMobile(EditProfileMobileDto editProfileMobileDto) {
//        User user = userRepository.findById(editProfileMobileDto.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Optional.ofNullable(editProfileMobileDto.getFirst_name()).ifPresent(user::setFirst_name);
//        Optional.ofNullable(editProfileMobileDto.getLast_name()).ifPresent(user::setLast_name);
//        Optional.ofNullable(editProfileMobileDto.getEmail()).ifPresent(user::setEmail);
//        Optional.ofNullable(editProfileMobileDto.getPhone()).ifPresent(user::setPhone);
//        Optional.ofNullable(editProfileMobileDto.getFunction()).ifPresent(user::setFunction);
//        Optional.ofNullable(editProfileMobileDto.getAddress()).ifPresent(user::setAddress);
//
//        Optional.ofNullable(editProfileMobileDto.getProfile_photo())
//                .ifPresent(photo -> user.setProfile_photo(Base64.getDecoder().decode(photo)));
//
//        Optional.ofNullable(editProfileMobileDto.getCompany_logo())
//                .ifPresent(photo -> user.setCompany_logo(Base64.getDecoder().decode(photo)));
//
//        Optional.ofNullable(editProfileMobileDto.getPassword())
//                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
//
//        Optional.ofNullable(editProfileMobileDto.getContact_person()).ifPresent(user::setContact_person);
//        Optional.ofNullable(editProfileMobileDto.getContact_person_phone()).ifPresent(user::setContact_person_phone);
//        Optional.ofNullable(editProfileMobileDto.getReport_to()).ifPresent(user::setReport_to);
//        Optional.ofNullable(editProfileMobileDto.getCompany_name()).ifPresent(user::setCompany_name);
//        Optional.ofNullable(editProfileMobileDto.getSmoking()).ifPresent(user::setSmoking);
//        Optional.ofNullable(editProfileMobileDto.getAlcoholic()).ifPresent(user::setAlcoholic);
//        Optional.ofNullable(editProfileMobileDto.getMedications()).ifPresent(user::setMedications);
//        Optional.ofNullable(editProfileMobileDto.getDrugs()).ifPresent(user::setDrugs);
//        Optional.ofNullable(editProfileMobileDto.getDiseases()).ifPresent(user::setDiseases);
//        Optional.ofNullable(editProfileMobileDto.getPin()).ifPresent(user::setPin);
//        Optional.ofNullable(editProfileMobileDto.getBlood_type()).ifPresent(user::setBlood_type);
//
//        return userRepository.save(user);
//    }
//
//    @Override
//    public PinSettingsDto getPinSettings() {
//        String username = getCurrentUsername();
//        User authUser = userRepository.findByEmail(username);
//        PinSettingsDto pinSettingsDto = new PinSettingsDto();
//        pinSettingsDto.setPin(authUser.getPin());
//        return pinSettingsDto;
//    }
//
//    @Override
//    public User updateUserPin(PinSettingsDto pinSettingsDto) {
//        String username = getCurrentUsername();
//        User authUser = userRepository.findByEmail(username);
//        authUser.setPin(pinSettingsDto.getPin());
//        return userRepository.save(authUser);
//    }
//
//    @Override
//    public UserTermsDto getUserTerms() {
//        String username = getCurrentUsername();
//        User authUser = userRepository.findByEmail(username);
//        UserTermsDto userTermsDto = new UserTermsDto();
//        if(authUser.getTerms_accepted() != null) {
//            userTermsDto.setTerms_accepted(authUser.getTerms_accepted());
//        }
//        else {
//            userTermsDto.setTerms_accepted(false);}
//
//        return userTermsDto;
//    }
//
//    @Override
//    public User updateUserTerms(UserTermsDto userTermsDto) {
//        String username = getCurrentUsername();
//        User authUser = userRepository.findByEmail(username);
//        authUser.setTerms_accepted(userTermsDto.getTerms_accepted());
//        return userRepository.save(authUser);
//    }
//
//    @Override
//    public boolean isUserInSite(Long userId, Double longitude, Double latitude) {
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isEmpty()) {
//            return false;
//        }
//        User user = userOptional.get();
//        Long siteId = user.getSiteId();
//        // Find the site
//        Optional<Site> siteOptional = siteRepository.findById(siteId);
//        if (siteOptional.isEmpty()) {
//            return false;
//        }
//        Site site = siteOptional.get();
//        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
//
//        // Check if the user's location is inside the site's plan
//        return site.getPlan2d() != null && site.getPlan2d().covers(point);
//    }
}

package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Worker.*;
import com.imense.loneworking.application.service.serviceInterface.UserServiceSynchro;
import com.imense.loneworking.domain.entity.Site;
import com.imense.loneworking.domain.entity.SiteSynchro;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.UserSynchro;
import com.imense.loneworking.domain.repository.SiteSynchroRepository;
import com.imense.loneworking.domain.repository.UserSynchroRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImplSynchro implements UserServiceSynchro {
    private final UserSynchroRepository userRepository;
    private final SiteSynchroRepository siteRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory;
    private final WebClient webClient;

    public UserServiceImplSynchro(UserSynchroRepository userRepository, SiteSynchroRepository siteRepository, PasswordEncoder passwordEncoder, WebClient.Builder webClientBuilder) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.passwordEncoder = passwordEncoder;
        this.geometryFactory = new GeometryFactory();
        this.webClient = webClientBuilder.baseUrl("https://core-dev.safetytracker.ma/api/v1").build();
    }

    @Override
    public List<UserDashboardDto> getAllUsersForDashboard(Long tenantId) {
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";

        List<Map<String, Object>> users = fetchUsersData(tenantId, token).block();

        // Transform the response into SiteInfoDto format
        return users.stream()
                .map(this::mapToUsersDahboardDto)
                .collect(Collectors.toList());
    }
    private Mono<List<Map<String, Object>>> fetchUsersData(Long tenantId, String token) {
        String url = "/loneworking/users/" + tenantId;

        return webClient
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (List<Map<String, Object>>) response.get("data"));
    }
    private UserDashboardDto mapToUsersDahboardDto(Map<String, Object> userData) {
        UserDashboardDto userDashboardDto = new UserDashboardDto();

        // Convert user ID to Long safely
        Object userIdObj = userData.get("id");
        Long userRefId = (userIdObj instanceof Integer) ? Long.valueOf((Integer) userIdObj) : (Long) userIdObj;

        Optional<UserSynchro> user = userRepository.findByRefId(userRefId);
        if (user.isPresent()) {
            userDashboardDto.setPosition(user.get().getPosition());
            userDashboardDto.setStatus(user.get().getConnectionStatus());
        }

        // Set basic user fields
        userDashboardDto.setId(userRefId);  // Already converted above
        userDashboardDto.setFirst_name((String) userData.get("first_name"));
        userDashboardDto.setLast_name((String) userData.get("last_name"));
        userDashboardDto.setProfile_photo((String) userData.get("profile_photo"));
        userDashboardDto.setPhone((String) userData.get("phone"));

        // Handle nested tenant data (check for null)
        Map<String, Object> tenantData = (Map<String, Object>) userData.get("tenant");
        if (tenantData != null) {
            Object tenantIdObj = tenantData.get("id");
            Long tenantId = (tenantIdObj instanceof Integer) ? Long.valueOf((Integer) tenantIdObj) : (Long) tenantIdObj;
            userDashboardDto.setCompany_id(tenantId);
            userDashboardDto.setCompany_name((String) tenantData.get("name"));
        } else {
            userDashboardDto.setCompany_name("N/A");  // Default value if tenant is missing
        }

        // Handle nested site data (check for null)
        Map<String, Object> siteData = (Map<String, Object>) userData.get("site");
        if (siteData != null) {
            Object siteIdObj = siteData.get("id");
            Long siteId = (siteIdObj instanceof Integer) ? Long.valueOf((Integer) siteIdObj) : (Long) siteIdObj;
            userDashboardDto.setSite_id(siteId);
            userDashboardDto.setSite_name((String) siteData.get("name"));
        } else {
            userDashboardDto.setSite_id(null);  // Default value if site is missing
        }


        return userDashboardDto;
    }


    @Override
    public List<WorkerInfoDto> getAllUsersForTable(Long tenantId) {
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";

        // Fetch users from the API
        List<Map<String, Object>> usersFromApi = fetchUsersData(tenantId, token).block();

        // Map the API response data to WorkerInfoDto, and fetch additional data from local DB
        return usersFromApi.stream()
                .map(userData -> {
                    WorkerInfoDto workerDto = mapToWorkerInfoDto(userData);

                    // Fetch additional data from the local database using the userRefId
                    Long userRefId = workerDto.getId();
                    Optional<UserSynchro> localUser = userRepository.findByRefId(userRefId);

                    // If the user exists in the local database, populate the missing fields
                    if (localUser.isPresent()) {
                        UserSynchro localUserData = localUser.get();
                        workerDto.setContact_person_phone(localUserData.getContact_person_phone());
                        workerDto.setContact_person(localUserData.getContact_person());
                        workerDto.setBlood_type(localUserData.getBlood_type());
                        workerDto.setDiseases(localUserData.getDiseases());
                        workerDto.setMedications(localUserData.getMedications());
                        workerDto.setAlcoholic(localUserData.getAlcoholic());
                        workerDto.setSmoking(localUserData.getSmoking());
                    }

                    return workerDto;
                })
                .collect(Collectors.toList());
    }


    private WorkerInfoDto mapToWorkerInfoDto(Map<String, Object> userData) {
        WorkerInfoDto workerDto = new WorkerInfoDto();

        // Convert user ID to Long safely
        Object userIdObj = userData.get("id");
        Long userRefId = (userIdObj instanceof Integer) ? Long.valueOf((Integer) userIdObj) : (Long) userIdObj;
        workerDto.setId(userRefId);

        // Set API fields
        workerDto.setFirst_name((String) userData.get("first_name"));
        workerDto.setLast_name((String) userData.get("last_name"));
        workerDto.setEmail((String) userData.get("email"));
        workerDto.setPhone((String) userData.get("phone"));
        workerDto.setCreated_at((String) userData.get("created_at"));

        // Set profile photo
        String profilePhotoUrl = (String) userData.get("profile_photo");
        workerDto.setProfile_photo(profilePhotoUrl);

        // Set company logo
        Map<String, Object> tenantData = (Map<String, Object>) userData.get("tenant");
        if (tenantData != null) {
            workerDto.setCompany_logo((String) tenantData.get("logo"));
        }

        // Set site name
        Map<String, Object> siteData = (Map<String, Object>) userData.get("site");
        if (siteData != null) {
            workerDto.setSite_name((String) siteData.get("name"));
        }

        // Set report to
        Map<String, Object> reportToData = (Map<String, Object>) userData.get("report_to");
        if (reportToData != null) {
            workerDto.setReport_to_first_name((String) reportToData.get("first_name"));
            workerDto.setReport_to_last_name((String) reportToData.get("last_name"));
        }

        // Set function
        Map<String, Object> functionData = (Map<String, Object>) userData.get("function");
        if (functionData != null) {
            workerDto.setFunction((String) functionData.get("name"));
            workerDto.setDepartment((String) functionData.get("departement"));
        }

        return workerDto;
    }

    @Override
    public AuthenticatedUserDto getAuthenticatedUser(Long userId) {
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";

        // Fetch user data from the external API
        Map<String, Object> userData = fetchAuthenticatedUserData(userId, token).block();

        // Map the API data to AuthenticatedUserDto
        assert userData != null;

        return mapToAuthenticatedUserDto(userData);
    }


    private Mono<Map<String, Object>> fetchAuthenticatedUserData(Long userId, String token) {
        String url = "/client/users/manage/" + userId;

        return webClient
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})  // Deserialize the full response
                .map(response -> (Map<String, Object>) response.get("user"));  // Extract the "user" field
    }


    private AuthenticatedUserDto mapToAuthenticatedUserDto(Map<String, Object> userData) {
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto();

        // Map API data to DTO
        Object userIdObj = userData.get("id");
        Long userId = (userIdObj instanceof Integer) ? Long.valueOf((Integer) userIdObj) : (Long) userIdObj;
        authenticatedUserDto.setId(userId);

        authenticatedUserDto.setFirst_name((String) userData.get("first_name"));
        authenticatedUserDto.setLast_name((String) userData.get("last_name"));
        authenticatedUserDto.setEmail((String) userData.get("email"));
        authenticatedUserDto.setPhone((String) userData.get("phone"));
        authenticatedUserDto.setProfile_photo((String) userData.get("profile_photo"));
        authenticatedUserDto.setTerms_accepted((Boolean) userData.get("terms_accepted"));

        // Map company (tenant) details
        Map<String, Object> tenantData = (Map<String, Object>) userData.get("tenant");
        if (tenantData != null) {
            authenticatedUserDto.setCompany_id((Long) tenantData.get("id"));
            authenticatedUserDto.setCompany_name((String) tenantData.get("name"));
            authenticatedUserDto.setCompany_logo((String) tenantData.get("logo"));
            authenticatedUserDto.setCompany_email((String) tenantData.get("email"));
            authenticatedUserDto.setCompany_phone((String) tenantData.get("phone"));
            authenticatedUserDto.setAddress((String) tenantData.get("address"));
        }

        // Map site details
        Map<String, Object> siteData = (Map<String, Object>) userData.get("site");
        if (siteData != null) {
            authenticatedUserDto.setSite_id(Long.valueOf(siteData.get("id").toString()));
            authenticatedUserDto.setSite_name((String) siteData.get("name"));
        }

        // Map function
        Map<String, Object> functionData = (Map<String, Object>) userData.get("function");
        if (functionData != null) {
            authenticatedUserDto.setFunction((String) functionData.get("name"));
        }

        return authenticatedUserDto;
    }

    @Override
    public Mono<UserSynchro> addWorker(WorkerCreationDto workerCreationDto) {
        String url = "/client/users/manage";
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";


        // Build form-data payload using MultipartBodyBuilder
        MultipartBodyBuilder bodyBuilder = getAddBodyBuilder(workerCreationDto);

        return webClient
                .post()
                .uri(url)
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);  // Correct Content-Type
                })
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    // Check if the API call was successful
                    if (Boolean.TRUE.equals(response.get("success"))) {
                        // Extract the list of users
                        List<Map<String, Object>> usersCreated = (List<Map<String, Object>>) response.get("users_created");

                        // Ensure the list is not empty and process the first user
                        if (usersCreated != null && !usersCreated.isEmpty()) {
                            Map<String, Object> userResponseData = usersCreated.get(0);  // Get the first user from the list
                            Long id = Long.valueOf(userResponseData.get("id").toString());

                            // Create UserSynchro with the returned id
                            UserSynchro userSynchro = new UserSynchro();
                            userSynchro.setRefId(id);
                            userSynchro.setSiteId(workerCreationDto.getSite_id());
                            // Save the UserSynchro entity
                            userRepository.save(userSynchro);

                            // Return userSynchro
                            return Mono.just(userSynchro);
                        } else {
                            return Mono.error(new RuntimeException("No users created"));
                        }
                    } else {
                        return Mono.error(new RuntimeException("Failed to add user: " + response.get("message")));
                    }
                });
    }

    private static MultipartBodyBuilder getAddBodyBuilder(WorkerCreationDto workerCreationDto) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
//        bodyBuilder.part("users[0][profile_photo]", workerCreationDto.getProfile_photo());
        bodyBuilder.part("users[0][first_name]", workerCreationDto.getFirst_name());
        bodyBuilder.part("users[0][last_name]", workerCreationDto.getLast_name());
        bodyBuilder.part("users[0][email]", workerCreationDto.getEmail());
        bodyBuilder.part("users[0][phone]", workerCreationDto.getPhone());
        bodyBuilder.part("users[0][tenant_id]", workerCreationDto.getCompany_id());
        bodyBuilder.part("users[0][report_to]", workerCreationDto.getReport_to_id());
        bodyBuilder.part("users[0][department_id]", workerCreationDto.getDepartment_id());
        bodyBuilder.part("users[0][function_id]", workerCreationDto.getFunction_id());
        bodyBuilder.part("users[0][site_id]", workerCreationDto.getSite_id());
        bodyBuilder.part("users[0][password]", workerCreationDto.getPassword());
        return bodyBuilder;
    }

    @Override
    public Mono<Map> updateWorker(Long userId, WorkerCreationDto workerCreationDto) {
        String url = "/client/profile/" + userId + "/update?_method=put"; // /client/profile/52/update
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";
        UserSynchro user = userRepository.findByRefId(userId).get();
        // Build form-data payload using MultipartBodyBuilder without "users[0]" notation
        MultipartBodyBuilder bodyBuilder = getUpdateBodyBuilder(workerCreationDto);

        return webClient
                .post()  // WebClient uses POST with _method=put to simulate PUT request
                .uri(url)
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                })
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(Map.class)  // No need to return a custom object, just return the response from the API
                .doOnNext(response -> {
                    if (!Boolean.TRUE.equals(response.get("success"))) {
                        if (workerCreationDto.getSite_id() != null) {
                            user.setSiteId(workerCreationDto.getSite_id());
                        }
                        throw new RuntimeException("Failed to update user: " + response.get("message"));
                    }
                });
    }

    private static MultipartBodyBuilder getUpdateBodyBuilder(WorkerCreationDto workerCreationDto) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
//      bodyBuilder.part("profile_photo", workerCreationDto.getProfile_photo());
        bodyBuilder.part("first_name", workerCreationDto.getFirst_name());
        bodyBuilder.part("last_name", workerCreationDto.getLast_name());
        bodyBuilder.part("email", workerCreationDto.getEmail());
        bodyBuilder.part("phone", workerCreationDto.getPhone());
        bodyBuilder.part("tenant_id", workerCreationDto.getCompany_id());
        bodyBuilder.part("report_to", workerCreationDto.getReport_to_id());
        bodyBuilder.part("department_id", workerCreationDto.getDepartment_id());
        bodyBuilder.part("function_id", workerCreationDto.getFunction_id());
        bodyBuilder.part("site_id", workerCreationDto.getSite_id());  // Using "site_id" directly
        bodyBuilder.part("password", workerCreationDto.getPassword());
        return bodyBuilder;
    }

    @Override
    public Mono<Map<String, Object>> deleteWorker(Long workerId) {
        String url = "/client/users/manage?ids[0]=" + workerId;
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";

        return webClient
                .delete()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(token))  // Add Bearer token
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {}) // Return the response as a map
                .doOnNext(response -> {
                    if (!Boolean.TRUE.equals(response.get("success"))) {
                        throw new RuntimeException("Failed to delete worker from external system.");
                    }
                });
    }

    @Override
    public void deletWorkerFromDatabase(Long workerId) {
        Optional<UserSynchro> userSynchro = userRepository.findByRefId(workerId);
        userSynchro.ifPresent(userRepository::delete);
    }

    @Override
    public Mono<Map<String, Object>> editProfileUser(EditProfileUserDto editProfileUserDto) {
        String url = "/client/profile/my-profile-update?_method=put"; // /client/profile/52/update
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";

        // Build form-data payload using MultipartBodyBuilder without "users[0]" notation
        MultipartBodyBuilder bodyBuilder = getEditProfileBodyBuilder(editProfileUserDto);

        return webClient
                .post()  // WebClient uses POST with _method=put to simulate PUT request
                .uri(url)
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                })
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})  // Correct deserialization with parameterized type
                .doOnNext(response -> {
                    if (!Boolean.TRUE.equals(response.get("success"))) {
                        throw new RuntimeException("Failed to update user: " + response.get("message"));
                    }
                });
    }

    private static MultipartBodyBuilder getEditProfileBodyBuilder(EditProfileUserDto editProfileUserDto) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        // Add only non-null parts to the body
        if (editProfileUserDto.getProfile_photo() != null) {
            bodyBuilder.part("profile_photo", editProfileUserDto.getProfile_photo());
        }
        if (editProfileUserDto.getFirst_name() != null) {
            bodyBuilder.part("first_name", editProfileUserDto.getFirst_name());
        }
        if (editProfileUserDto.getLast_name() != null) {
            bodyBuilder.part("last_name", editProfileUserDto.getLast_name());
        }
        if (editProfileUserDto.getEmail() != null) {
            bodyBuilder.part("email", editProfileUserDto.getEmail());
        }
        if (editProfileUserDto.getPhone() != null) {
            bodyBuilder.part("phone", editProfileUserDto.getPhone());
        }
        if (editProfileUserDto.getFunction_id() != null) {
            bodyBuilder.part("function_id", editProfileUserDto.getFunction_id());
        }
        if (editProfileUserDto.getAddress() != null) {
            bodyBuilder.part("address", editProfileUserDto.getAddress());
        }

        return bodyBuilder;
    }

    @Override
    public Mono<Map<String, Object>> changePasswordUser(ChangePasswordDto changePasswordDto) {
        String url = "/client/profile/password?_method=put";
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95"; // Your token

        // Build the form-data payload using MultipartBodyBuilder
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("current_password", changePasswordDto.getCurrentPassword());
        bodyBuilder.part("password", changePasswordDto.getNewPassword());
        bodyBuilder.part("password_confirmation", changePasswordDto.getConfirmPassword());

        return webClient
                .post()
                .uri(url)
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);  // Set to multipart/form-data
                })
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnNext(response -> {
                    if (!Boolean.TRUE.equals(response.get("success"))) {
                        throw new RuntimeException("Failed to change password: " + response.get("message"));
                    }
                });
    }

    @Override
    public Mono<EditProfileMobileDto> getUserForMobileSettings(Long userId) {
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";

        // Step 1: Fetch data from external API
        return fetchUserProfileFromApi(userId, token)
                .flatMap(userData -> {
                    // Step 2: Fetch additional fields from the local database
                    Optional<UserSynchro> localUserData = userRepository.findByRefId(userId);

                    // Step 3: Map API and local data to EditProfileMobileDto
                    EditProfileMobileDto profile = mapToEditProfileMobileDto(userData, localUserData.orElse(null));

                    return Mono.just(profile);
                });
    }

    private Mono<Map<String, Object>> fetchUserProfileFromApi(Long userId, String token) {
        String url = "/client/users/manage/" + userId;

        return webClient
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})  // Deserialize the full response
                .map(response -> (Map<String, Object>) response.get("user"));  // Extract the "user" field
    }

    private EditProfileMobileDto mapToEditProfileMobileDto(Map<String, Object> userData, UserSynchro localUserData) {
        EditProfileMobileDto profile = new EditProfileMobileDto();

        // Map fields from the API
        Object userIdObj = userData.get("id");
        Long userId = (userIdObj instanceof Integer) ? Long.valueOf((Integer) userIdObj) : (Long) userIdObj;
        profile.setId(userId);
        profile.setFirst_name((String) userData.get("first_name"));
        profile.setLast_name((String) userData.get("last_name"));
        profile.setEmail((String) userData.get("email"));
        profile.setPhone((String) userData.get("phone"));
        profile.setProfile_photo((String) userData.get("profile_photo"));

        Map<String, Object> tenantData = (Map<String, Object>) userData.get("tenant");
        if (tenantData != null) {
            profile.setCompany_id(Long.valueOf(tenantData.get("id").toString()));
            profile.setAddress((String) tenantData.get("address"));
            profile.setCompany_name((String) tenantData.get("name"));
        }

        Map<String, Object> functionData = (Map<String, Object>) userData.get("function");
        if (functionData != null) {
            profile.setFunction_id(Long.valueOf(functionData.get("id").toString()));
            profile.setReport_to_first_name((String) functionData.get("name"));
        }

        // Set report to
        Map<String, Object> reportToData = (Map<String, Object>) userData.get("report_to");
        if (reportToData != null) {
            profile.setReport_to_first_name((String) reportToData.get("first_name"));
            profile.setReport_to_last_name((String) reportToData.get("last_name"));
        }

        // Map fields from the local database (UserSynchro)
        if (localUserData != null) {
            profile.setContact_person(localUserData.getContact_person());
            profile.setContact_person_phone(localUserData.getContact_person_phone());

            // Health info
            profile.setBlood_type(localUserData.getBlood_type());
            profile.setDiseases(localUserData.getDiseases());
            profile.setMedications(localUserData.getMedications());
            profile.setAlcoholic(localUserData.getAlcoholic());
            profile.setSmoking(localUserData.getSmoking());
            profile.setDrugs(localUserData.getDrugs());
        }

        return profile;
    }


    @Override
    public Mono<Map<String, Object>> settingsMobile(EditProfileMobileDto editProfileMobileDto) {
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";

        // Step 1: Update external API profile
        return updateExternalUserProfile(editProfileMobileDto, token)
                .flatMap(apiResponse -> {
                    // Step 2: Update local database fields if they are provided in the DTO
                    updateLocalUserProfile(editProfileMobileDto.getId(), editProfileMobileDto);
                    return Mono.just(apiResponse); // Return the API response
                });
    }

    private Mono<Map<String, Object>> updateExternalUserProfile(EditProfileMobileDto editProfileMobileDto, String token) {
        String url = "/client/profile/my-profile-update?_method=put";  // The external API endpoint

        // Build form-data payload using MultipartBodyBuilder for external API
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        if (editProfileMobileDto.getProfile_photo() != null) {
            bodyBuilder.part("profile_photo", editProfileMobileDto.getProfile_photo());
        }
        if (editProfileMobileDto.getFirst_name() != null) {
            bodyBuilder.part("first_name", editProfileMobileDto.getFirst_name());
        }
        if (editProfileMobileDto.getLast_name() != null) {
            bodyBuilder.part("last_name", editProfileMobileDto.getLast_name());
        }
        if (editProfileMobileDto.getEmail() != null) {
            bodyBuilder.part("email", editProfileMobileDto.getEmail());
        }
        if (editProfileMobileDto.getPhone() != null) {
            bodyBuilder.part("phone", editProfileMobileDto.getPhone());
        }
        if (editProfileMobileDto.getFunction_id() != null) {
            bodyBuilder.part("function_id", editProfileMobileDto.getFunction_id());
        }
        if (editProfileMobileDto.getCompany_id() != null) {
            bodyBuilder.part("company_id", editProfileMobileDto.getCompany_id());
        }
        if (editProfileMobileDto.getAddress() != null) {
            bodyBuilder.part("address", editProfileMobileDto.getAddress());
        }

        return webClient
                .post()
                .uri(url)
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);  // Set content type as multipart/form-data
                })
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})  // Deserialize API response
                .doOnNext(response -> {
                    if (!Boolean.TRUE.equals(response.get("success"))) {
                        throw new RuntimeException("Failed to update external user profile: " + response.get("message"));
                    }
                });
    }

    private void updateLocalUserProfile(Long userId, EditProfileMobileDto editProfileMobileDto) {
        // Fetch the local user data by userId (UserSynchro)
        Optional<UserSynchro> localUserOpt = userRepository.findByRefId(userId);
        if (localUserOpt.isPresent()) {
            UserSynchro localUser = localUserOpt.get();

            // Update only the provided non-null fields
            if (editProfileMobileDto.getContact_person() != null) {
                localUser.setContact_person(editProfileMobileDto.getContact_person());
            }
            if (editProfileMobileDto.getContact_person_phone() != null) {
                localUser.setContact_person_phone(editProfileMobileDto.getContact_person_phone());
            }
            if (editProfileMobileDto.getBlood_type() != null) {
                localUser.setBlood_type(editProfileMobileDto.getBlood_type());
            }
            if (editProfileMobileDto.getDiseases() != null) {
                localUser.setDiseases(editProfileMobileDto.getDiseases());
            }
            if (editProfileMobileDto.getMedications() != null) {
                localUser.setMedications(editProfileMobileDto.getMedications());
            }
            if (editProfileMobileDto.getAlcoholic() != null) {
                localUser.setAlcoholic(editProfileMobileDto.getAlcoholic());
            }
            if (editProfileMobileDto.getSmoking() != null) {
                localUser.setSmoking(editProfileMobileDto.getSmoking());
            }
            if (editProfileMobileDto.getDrugs() != null) {
                localUser.setDrugs(editProfileMobileDto.getDrugs());
            }

            // Save the updated user data back to the local database
            userRepository.save(localUser);
        }
    }

    @Override
    public Mono<Void> updateUserPin(PinSettingsUpdate pinSettingsUpdate) {
        String token = "1445|O67XSkWJ9PM9t2bSsfeTihramzyg0KcmuO0Qd7SN71edec95";  // Example token, replace with actual token

        // Step 1: Update the pin using the external API
        return updatePinExternalApi(pinSettingsUpdate, token)
                .then();  // Just complete without returning the updated user
    }

    private Mono<Map<String, Object>> updatePinExternalApi(PinSettingsUpdate pinSettingsUpdate, String token) {
        String url = "/client/profile/pin?_method=put";  // External API endpoint for pin update

        // Build form-data payload using MultipartBodyBuilder
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("current_pin", pinSettingsUpdate.getCurrentPin());
        bodyBuilder.part("new_pin", pinSettingsUpdate.getNewPin());
        bodyBuilder.part("confirm_pin", pinSettingsUpdate.getConfirmPin());

        return webClient
                .post()
                .uri(url)
                .headers(headers -> {
                    headers.setBearerAuth(token);
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);  // Content type: multipart/form-data
                })
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnNext(response -> {
                    // Handle the success/failure of the API call
                    if (!Boolean.TRUE.equals(response.get("success"))) {
                        throw new RuntimeException("Failed to update pin: " + response.get("message"));
                    }
                });
    }

    @Override
    public boolean isUserInSite(Long userId, Double longitude, Double latitude) {
        Optional<UserSynchro> userOptional = userRepository.findByRefId(userId);
        if (userOptional.isEmpty()) {
            return false;
        }
        UserSynchro user = userOptional.get();
        Long siteId = user.getSiteId();
        // Find the site
        Optional<SiteSynchro> siteOptional = siteRepository.findById(siteId);
        if (siteOptional.isEmpty()) {
            return false;
        }
        SiteSynchro site = siteOptional.get();
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

        // Check if the user's location is inside the site's plan
        return site.getPlan2d() != null && site.getPlan2d().covers(point);
    }





}

package com.imense.loneworking.application.service.serviceImpl;

import com.imense.loneworking.application.dto.Dashboard.UserDashboardDto;
import com.imense.loneworking.application.dto.Site.SiteInfoDto;
import com.imense.loneworking.application.dto.Worker.AuthenticatedUserDto;
import com.imense.loneworking.application.dto.Worker.WorkerInfoDto;
import com.imense.loneworking.application.service.serviceInterface.UserServiceSynchro;
import com.imense.loneworking.domain.entity.User;
import com.imense.loneworking.domain.entity.UserSynchro;
import com.imense.loneworking.domain.repository.SiteRepository;
import com.imense.loneworking.domain.repository.SiteSynchroRepository;
import com.imense.loneworking.domain.repository.UserRepository;
import com.imense.loneworking.domain.repository.UserSynchroRepository;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        // Map company (tenant) details
        Map<String, Object> tenantData = (Map<String, Object>) userData.get("tenant");
        if (tenantData != null) {
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








}

package com.imense.loneworking.application.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imense.loneworking.application.dto.Dashboard.ZoneDashboardDto;
import com.imense.loneworking.application.dto.Qrcode.ZoneQrCodeDto;
import com.imense.loneworking.application.dto.Zone.ZoneCreationDto;
import com.imense.loneworking.application.dto.Zone.ZoneInfoDto;
import com.imense.loneworking.application.dto.Zone.ZoneUpdateDto;
import com.imense.loneworking.application.service.serviceInterface.SafetyTrackerZoneService;
import com.imense.loneworking.domain.entity.Zone;

import org.json.JSONArray;
import org.json.JSONObject;
import com.imense.loneworking.domain.entity.ZoneSynchro;
import com.imense.loneworking.domain.repository.ZoneRepository;

import com.imense.loneworking.domain.repository.ZoneSynchroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class SafetyTrackerZoneServiceImpl implements SafetyTrackerZoneService {
    private final WebClient webClient;
    private final ZoneSynchroRepository zoneSynchroRepository;
    public SafetyTrackerZoneServiceImpl(WebClient.Builder webClientBuilder,ZoneSynchroRepository zoneSynchroRepository) {
        this.webClient = webClientBuilder.baseUrl("https://core-dev.safetytracker.ma/api/v1").build();
        this.zoneSynchroRepository=zoneSynchroRepository;

    }

    @Override
    public List<ZoneInfoDto> getSiteZoneInfo() {
        List<ZoneInfoDto> zoneInfoDtoList = new ArrayList<>();
        int siteId=1;
        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";

        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loneworking/zones/{siteId}")
                            .build(siteId))  // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token)  // Use existing token
                    .retrieve()
                    .bodyToMono(String.class)  // Get the response as a String
                    .block();

            // Parse the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            if (rootNode.path("success").asBoolean() && rootNode.path("data").isArray()) {
                for (JsonNode zoneNode : rootNode.path("data")) {
                    ZoneInfoDto zoneInfoDto = new ZoneInfoDto();
                    zoneInfoDto.setZone_id(zoneNode.path("id").asLong());
                    zoneInfoDto.setSiteName(zoneNode.path("site").path("name").asText());
                    zoneInfoDto.setZoneName(zoneNode.path("name").asText());
                    //zoneInfoDto.setZoneCreatedAt(zoneNode.path("created_at").asText());
                    // Check for tenant information
                    JsonNode tenantNode = zoneNode.path("tenant");
                    if (tenantNode != null && !tenantNode.isNull()) {
                        zoneInfoDto.setCompanyName(tenantNode.path("name").asText()); // Assuming tenant has a name field
                    } else {
                        zoneInfoDto.setCompanyName(null);
                    }
                    Optional<ZoneSynchro> optionalZoneSynchro = zoneSynchroRepository.findByRefId(zoneInfoDto.getZone_id());

                    if (optionalZoneSynchro.isPresent()) {
                        ZoneSynchro zoneSynchro = optionalZoneSynchro.get();
                        zoneInfoDto.setZonePlan(zoneSynchro.getPlan());
                    } else {
                        zoneInfoDto.setZonePlan(null);
                    }

                    zoneInfoDtoList.add(zoneInfoDto);
                }
            }


            return zoneInfoDtoList;  // Return the list of ZoneInfoDto objects

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Zone not found for siteId: " + siteId);
            } else {
                throw new RuntimeException("Failed to fetch zone data: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred: " + e.getMessage());
        }
    }


    @Override
    public ZoneSynchro addZone(ZoneCreationDto zoneCreationDto) {

        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";
        try {
            String requestBody = "{"
                    + "\"zones\": ["
                    + "{"
                    + "\"name\": \"" + zoneCreationDto.getName() + "\","
                    + "\"number\": \"0\","
                    + "\"levels\": [0]" // Ajoutez les niveaux ici
                    + "}"
                    + "]"
                    + "}";
            String response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/client/sites/{siteId}/zones/bulk-store")
                            .build(zoneCreationDto.getSiteId())) // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token) // Use existing token
                    .contentType(MediaType.APPLICATION_JSON) // Set content type
                    .bodyValue(requestBody) // Set the request body
                    .retrieve()
                    .bodyToMono(String.class) // Get the response as a String
                    .block();


System.out.println(response);


             response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loneworking/zones/{siteId}")
                            .build(zoneCreationDto.getSiteId()))  // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token)  // Use existing token
                    .retrieve()
                    .bodyToMono(String.class)  // Get the response as a String
                    .block();

            // Inside your method after retrieving the zones
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray zonesArray = jsonResponse.getJSONArray("data"); // Access the "data" array

            // Get the last zone in the array
            JSONObject lastZone = zonesArray.getJSONObject(zonesArray.length() - 1);
            long lastZoneId = lastZone.getInt("id"); // Extract the ID

            System.out.println("Last added Zone ID: " + lastZoneId); // Print the last added zone ID
            ZoneSynchro zoneSynchro=new ZoneSynchro();
            zoneSynchro.setPlan(zoneCreationDto.getPlanZone());
            zoneSynchro.setRefId(lastZoneId);
            zoneSynchroRepository.save(zoneSynchro);



            return null;

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Site not found for siteId: " + zoneCreationDto.getSiteId());
            } else {
                throw new RuntimeException("Failed to add zone: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred: " + e.getMessage());
        }

    }

    @Override
    public List<ZoneDashboardDto> getSiteZoneInfoDashboard(Long site_id) {
        List<ZoneDashboardDto> zoneDashboardDtos = new ArrayList<>();

        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";

        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loneworking/zones/{site_id}")
                            .build(site_id))  // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token)  // Use existing token
                    .retrieve()
                    .bodyToMono(String.class)  // Get the response as a String
                    .block();

            // Parse the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            if (rootNode.path("success").asBoolean() && rootNode.path("data").isArray()) {
                for (JsonNode zoneNode : rootNode.path("data")) {
                    ZoneDashboardDto zoneDashboardDto=new ZoneDashboardDto();
                    zoneDashboardDto.setId(zoneNode.path("id").asLong());
                    zoneDashboardDto.setName(zoneNode.path("name").asText());
                    zoneDashboardDto.setSite_id(zoneNode.path("site").path("id").asLong());
                    Optional<ZoneSynchro> optionalZoneSynchro = zoneSynchroRepository.findByRefId(zoneDashboardDto.getId());

                    if (optionalZoneSynchro.isPresent()) {
                        ZoneSynchro zoneSynchro = optionalZoneSynchro.get();
                        zoneDashboardDto.setPlan(zoneSynchro.getPlan());
                    } else {
                        zoneDashboardDto.setPlan(null);
                    }
                    zoneDashboardDtos.add(zoneDashboardDto);
                }
            }


            return zoneDashboardDtos;  // Return the list of ZoneInfoDto objects

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Zone not found for siteId: " + site_id);
            } else {
                throw new RuntimeException("Failed to fetch zone data: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public List<ZoneQrCodeDto> getZoneInfoQrCode(Long site_id) {
        List<ZoneQrCodeDto> zoneQrCodeDtos = new ArrayList<>();

        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";

        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/loneworking/zones/{site_id}")
                            .build(site_id))  // Build the URI with dynamic site ID
                    .header("Accept", "application/vnd.api+json")
                    .header("Authorization", "Bearer " + token)  // Use existing token
                    .retrieve()
                    .bodyToMono(String.class)  // Get the response as a String
                    .block();

            // Parse the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            if (rootNode.path("success").asBoolean() && rootNode.path("data").isArray()) {
                for (JsonNode zoneNode : rootNode.path("data")) {
                    ZoneQrCodeDto zoneQrCodeDto=new ZoneQrCodeDto();

                    zoneQrCodeDto.setZone_id(zoneNode.path("id").asLong());
                    zoneQrCodeDto.setZoneName(zoneNode.path("name").asText());

                    zoneQrCodeDtos.add(zoneQrCodeDto);
                }
            }


            return zoneQrCodeDtos;  // Return the list of ZoneInfoDto objects

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Zone not found for siteId: " + site_id);
            } else {
                throw new RuntimeException("Failed to fetch zone data: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public ZoneSynchro updateZone(Long zoneId, ZoneUpdateDto zoneUpdateDto) {

        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";
        try {
            // Effectuer la requête POST pour simuler un PUT
            String uri = UriComponentsBuilder.fromPath("/client/zones/{zoneId}")
                    .queryParam("_method", "put")
                    .buildAndExpand(zoneId)
                    .toUriString();
            String response = webClient.post()
                    .uri(uri) // URL avec méthode simulée
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token) // Utiliser le token d'autorisation
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Définir le type de contenu
                    .body(BodyInserters.fromFormData("name", zoneUpdateDto.getZoneName())) // Ajouter le champ 'name'
                    .retrieve()
                    .bodyToMono(String.class) // Obtenir la réponse en tant que String
                    .block(); // Bloquer pour attendre la réponse
            Optional<ZoneSynchro> optionalZoneSynchro = zoneSynchroRepository.findByRefId(zoneId);
            optionalZoneSynchro.ifPresent(zoneSynchro -> {
                zoneSynchro.setPlan(zoneUpdateDto.getPlanZone()); // Définir le nouveau plan
                zoneSynchroRepository.save(zoneSynchro); // Enregistrer l'objet mis à jour
            });


        }
        catch (Exception e) {
            // Gérer les exceptions
            System.err.println("Erreur lors de la mise à jour de la zone : " + e.getMessage());
            e.printStackTrace(); // Afficher la trace de la pile pour le débogage
        }
        return null;
    }

    @Override
    public void deleteZone(Long zoneId) {

        String token="1367|JJrkFTNSNUB2wW5on5HIdqaYWG6pZkrJcer1q96G5f71f6f1";

        try {
            String response = webClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/client/zones")
                            .queryParam("ids", zoneId)
                            .build())
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            zoneSynchroRepository.findByRefId(zoneId).ifPresent(zoneSynchroRepository::delete);

        } catch (Exception e) {
            // Handle exceptions
            System.err.println("Error occurred while trying to delete the zone: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
}

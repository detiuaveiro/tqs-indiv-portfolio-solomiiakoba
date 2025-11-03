package com.tqs.zeromonos.service;
import com.tqs.zeromonos.exception.*;
import org.springframework.cache.annotation.Cacheable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
@Slf4j
public class MunicipalityService {
    private final RestTemplate restTemplate;
    private static final String MUNICIPALITY_URL = "https://json.geoapi.pt/municipios";

    public MunicipalityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("municipalities")
    public List<String> getAllMunicipalities() {
        log.info("Fetching info from external api...");
        try {
            String[] municipalities = restTemplate.getForObject(MUNICIPALITY_URL, String[].class);
            if (municipalities == null || municipalities.length == 0) {
                log.warn("No municipalities received from external API.");
                throw new MunicipalityServiceException("Empty municipality list");
            }
            List<String> result = Arrays.asList(municipalities);
            Collections.sort(result);

            log.info("Successfully fetched {} municipalities", result.size());
            return result;
        } catch (Exception e) {
            log.error("Error while fetching municipalities from external api.", e);
            throw new MunicipalityServiceException(
                    "Failed to fetch municipalities", e);
        }
    }

    public boolean isValidMunicipality(String municipality) {
        try {
            return getAllMunicipalities().contains(municipality);
        } catch (Exception e) {
            log.error("Error validating municipality: {}", e.getMessage());
            return false;
        }
    }

}

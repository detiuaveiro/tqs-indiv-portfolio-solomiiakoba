package com.tqs.zeromonos;

import com.tqs.zeromonos.exception.MunicipalityServiceException;
import com.tqs.zeromonos.service.MunicipalityService;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)// For integration of Mockito framework
@MockitoSettings(strictness = Strictness.LENIENT)
class MunicipalityServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private MunicipalityService municipalityService;
    @Setter
    @Getter
    private static class TestMunicipalityDTO {
        private String nome;
        public TestMunicipalityDTO(String nome) {
            this.nome = nome;
        }
    }
    @Test
    void testExternalAPIReturnsMunicipalities() {
        String[] response = {"Aveiro", "Porto"};
        when(restTemplate.getForObject(anyString(), eq(String[].class))).thenReturn(response);
        List<String> municipalities = municipalityService.getAllMunicipalities();
        assertThat(municipalities).contains("Aveiro", "Porto");
        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }
    @Test
    void testExternalAPIFails() {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RestClientException("Connection failed"));
        assertThatThrownBy(() -> municipalityService.getAllMunicipalities())
                .isInstanceOf(MunicipalityServiceException.class)
                .hasMessageContaining("Failed to fetch municipalities");
    }
    @Test
    void testValidMunicipality() {
        String[] response = {"Aveiro", "Porto"};
        when(restTemplate.getForObject(anyString(), eq(String[].class))).thenReturn(response);
        boolean isValid = municipalityService.isValidMunicipality("Aveiro");
        assertThat(isValid).isTrue();
    }
    @Test
    void testInvalidMunicipality() {
        TestMunicipalityDTO[] response = {new TestMunicipalityDTO("Aveiro")};
        when(restTemplate.getForObject(anyString(), any())).thenReturn(response);
        boolean isValid = municipalityService.isValidMunicipality("Invalid municipality");
        assertThat(isValid).isFalse();
    }
    @Test
    void testAPIFailsDuringValidation() {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RestClientException("API Error"));
        boolean isValid = municipalityService.isValidMunicipality("Aveiro");
        assertThat(isValid).isFalse();
    }
}



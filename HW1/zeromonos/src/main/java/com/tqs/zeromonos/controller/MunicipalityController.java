package com.tqs.zeromonos.controller;

import com.tqs.zeromonos.service.MunicipalityService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping
    public List<String> getAllMunicipalities() {
        return municipalityService.getAllMunicipalities();
    }
}

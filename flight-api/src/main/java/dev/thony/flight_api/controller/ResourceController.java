package dev.thony.flight_api.controller;

import org.springframework.web.bind.annotation.RestController;

import dev.thony.flight_api.model.DTOs.AirplaneDTO;
import dev.thony.flight_api.model.Enums.ContinentEnum;
import dev.thony.flight_api.service.ResourceService;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController()
@RequestMapping("/api/resource")
public class ResourceController {
    private ResourceService resourceService;

    ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/airplanes")
    public ResponseEntity<List<AirplaneDTO>> getAllAirplanes() {
        List<AirplaneDTO> airplaneList = resourceService.getAllAirplanes();
        return ResponseEntity.ok(airplaneList);
    }

    @GetMapping("/continents")
    public ResponseEntity<List<ContinentEnum>> getAllContinents() {
        List<ContinentEnum> continentList = resourceService.getAllConinents();
        return ResponseEntity.ok(continentList);
    }
}

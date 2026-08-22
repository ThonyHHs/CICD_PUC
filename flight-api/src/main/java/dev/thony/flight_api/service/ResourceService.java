package dev.thony.flight_api.service;

import java.util.List;
import org.springframework.stereotype.Service;

import dev.thony.flight_api.model.DTOs.AirplaneDTO;
import dev.thony.flight_api.model.Enums.ContinentEnum;
import dev.thony.flight_api.repository.AirplaneRepository;

@Service
public class ResourceService {
    private AirplaneRepository airplaneRepository;

    ResourceService(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    public List<ContinentEnum> getAllConinents() {
        return List.of(ContinentEnum.values());
    }

    public List<AirplaneDTO> getAllAirplanes() {
        return airplaneRepository.findAll().stream()
                .map(airplane -> new AirplaneDTO(airplane.getCode(), airplane.getName()))
                .toList();
    }
}

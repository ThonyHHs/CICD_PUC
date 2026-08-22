package dev.thony.flight_api.model.DTOs;

import dev.thony.flight_api.model.AirportModel;
import dev.thony.flight_api.model.Enums.ContinentEnum;

public record AirportDTO(
    String code,
    String name,
    ContinentEnum continent,
    String country,
    String municipality,
    String wikipediaLink
) {
    public static AirportDTO from(AirportModel airport) {
        return new AirportDTO(
            airport.getCode(),
            airport.getName(),
            airport.getContinent(),
            airport.getCountry(),
            airport.getMunicipality(),
            airport.getWikipediaLink()
        );
    }
}        
    
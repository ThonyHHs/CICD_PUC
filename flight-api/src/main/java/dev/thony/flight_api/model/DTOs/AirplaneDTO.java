package dev.thony.flight_api.model.DTOs;

import dev.thony.flight_api.model.AirplaneModel;

public record AirplaneDTO(String code, String name) {
    public static AirplaneDTO from(AirplaneModel airplane) {
        return new AirplaneDTO(airplane.getCode(), airplane.getName());
    }
}

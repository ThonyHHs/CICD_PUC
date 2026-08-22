package dev.thony.flight_api.model.DTOs;

import java.time.Duration;

public record RouteDTO(
    AirportDTO departure,
    AirportDTO arrival,
    AirplaneDTO airplane,
    Duration flightTime,
    int distanceNm,
    String simbriefLink
) {}

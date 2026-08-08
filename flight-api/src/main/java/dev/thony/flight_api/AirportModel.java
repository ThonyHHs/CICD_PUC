package dev.thony.flight_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportModel {
    private String icao;
    private double latitude;
    private double longitude;
    private ContinentEnum continent;
    private AirplaneTypeEnum maxAirplaneSupported;
}

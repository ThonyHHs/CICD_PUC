package dev.thony.flight_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneModel {
    private String code;
    private String name;
    private int range;
    private int cruiseSpeed;
    private AirplaneTypeEnum type;
}

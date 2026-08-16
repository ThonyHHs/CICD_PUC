package dev.thony.flight_api.model;

import dev.thony.flight_api.model.Enums.AirplaneTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "airplane")
public class AirplaneModel {
    @Id
    @Column(length = 5)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    @Min(value = 1, message = "The range needs to be greater than zero")
    private int range;

    @Column(nullable = false)
    @Min(value = 1, message = "The speed needs to be greater than zero")
    private int speed;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AirplaneTypeEnum type;
}

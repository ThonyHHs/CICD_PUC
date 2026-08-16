package dev.thony.flight_api.model;

import dev.thony.flight_api.model.Enums.AirportTypeEnum;
import dev.thony.flight_api.model.Enums.ContinentEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "airport")
public class AirportModel {
    @Id
    @Column(length = 4)
    private String code;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = false)
    private ContinentEnum continent;

    @Column(length = 2, nullable = false)
    private String country;

    @Column(length = 100)
    private String municipality;

    @Column(name = "wikipedia_link")
    private String wikipediaLink;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AirportTypeEnum type;
}

package dev.thony.flight_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.thony.flight_api.model.AirportModel;
import dev.thony.flight_api.model.Enums.ContinentEnum;


public interface AirportRepository extends JpaRepository<AirportModel, String> {
    List<AirportModel> findByContinent(ContinentEnum continent);
}

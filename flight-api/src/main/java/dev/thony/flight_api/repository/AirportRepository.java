package dev.thony.flight_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.thony.flight_api.model.AirportModel;

public interface AirportRepository extends JpaRepository<AirportModel, String> {

}

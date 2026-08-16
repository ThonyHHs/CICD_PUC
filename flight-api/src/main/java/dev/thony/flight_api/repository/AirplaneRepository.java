package dev.thony.flight_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.thony.flight_api.model.AirplaneModel;

public interface AirplaneRepository extends JpaRepository<AirplaneModel, String> {

}

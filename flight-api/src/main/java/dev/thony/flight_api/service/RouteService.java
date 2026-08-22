package dev.thony.flight_api.service;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.thony.flight_api.model.AirplaneModel;
import dev.thony.flight_api.model.AirportModel;
import dev.thony.flight_api.model.DTOs.AirplaneDTO;
import dev.thony.flight_api.model.DTOs.AirportDTO;
import dev.thony.flight_api.model.DTOs.RouteDTO;
import dev.thony.flight_api.model.Enums.ContinentEnum;
import dev.thony.flight_api.repository.AirplaneRepository;
import dev.thony.flight_api.repository.AirportRepository;

@Service
public class RouteService {
    private final AirplaneRepository airplaneRepository;
    private final AirportRepository airportRepository;
    private final Random random = new Random();

    RouteService(AirplaneRepository airplaneRepository, AirportRepository airportRepository) {
        this.airplaneRepository = airplaneRepository;
        this.airportRepository = airportRepository;
    }

    public RouteDTO generateRoute(String airplaneCode, ContinentEnum continent) {
        AirplaneModel airplane = airplaneRepository.findById(airplaneCode)
                .orElseThrow(() -> new RuntimeException("Airplane Not Found!"));

        AirportModel departure = getDepartureAirport(airplane, continent);
        AirportModel arrival = getArrivalAirport(airplane, departure);

        int distance = calculateDistanceNm(departure, arrival);
        Duration flightTime = calculateFlightTime(distance, airplane.getSpeed());
        String simbriefLink = String.format("https://dispatch.simbrief.com/options/custom?type=%s&orig=%s&dest=%s",
                airplane.getCode(), departure.getCode(), arrival.getCode());

        return new RouteDTO(AirportDTO.from(departure), AirportDTO.from(arrival), AirplaneDTO.from(airplane), flightTime, distance, simbriefLink);
    }

    private int calculateDistanceNm(AirportModel departure, AirportModel arrival) {
        final int EARTH_RADIUS_KM = 6371;

        double lat1 = Math.toRadians(departure.getLatitude());
        double long1 = Math.toRadians(departure.getLongitude());
        double lat2 = Math.toRadians(arrival.getLatitude());
        double long2 = Math.toRadians(arrival.getLongitude());

        double deltaLat = lat2 - lat1;
        double deltaLong = long2 - long1;

        double sinHalfDeltaLat = Math.sin(deltaLat / 2);
        double sinHalfDeltaLong = Math.sin(deltaLong / 2);

        double a = Math.pow(sinHalfDeltaLat, 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(sinHalfDeltaLong, 2);

        double distance = 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(a));
        return (int) Math.round(distance / 1.852);
    }

    private Duration calculateFlightTime(int distanceNm, int airplaneSpeedKts) {
        double hours = (double) distanceNm / airplaneSpeedKts;
        return Duration.ofMinutes(Math.round(hours * 60));
    }

    private AirportModel getDepartureAirport(AirplaneModel airplane, ContinentEnum continent) {
        List<AirportModel> departureList = airportRepository.findByContinent(continent).stream()
                .filter(airport -> airport.getType().ordinal() >= airplane.getType().ordinal())
                .collect(Collectors.toList());

        if (departureList.isEmpty()) {
            throw new RuntimeException("No Airport found on continent {" + continent + "}!");
        }

        return departureList.get(random.nextInt(departureList.size()));
    }

    private AirportModel getArrivalAirport(AirplaneModel airplane, AirportModel departure) {
        List<AirportModel> arrivalList = airportRepository.findAll().stream()
                .filter(airport -> !airport.getCode().equals(departure.getCode()))
                .filter(airport -> calculateDistanceNm(departure, airport) < airplane.getRange() * 0.8)
                .filter(airport -> airport.getType().ordinal() >= airplane.getType().ordinal())
                .collect(Collectors.toList());

        if (arrivalList.isEmpty()) {
            throw new RuntimeException(
                    "No nearby Airport {" + departure.getCode() + "} that support the airplane type!");
        }

        return arrivalList.get(random.nextInt(arrivalList.size()));
    }
}

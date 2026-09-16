package dev.thony.flight_api;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.thony.flight_api.model.AirplaneModel;
import dev.thony.flight_api.model.AirportModel;
import dev.thony.flight_api.model.Enums.AirplaneTypeEnum;
import dev.thony.flight_api.model.Enums.AirportTypeEnum;
import dev.thony.flight_api.model.Enums.ContinentEnum;
import dev.thony.flight_api.repository.AirplaneRepository;
import dev.thony.flight_api.repository.AirportRepository;
import dev.thony.flight_api.service.RouteService;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {
    @Mock
    private AirplaneRepository airplaneRepository;
    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private RouteService routeService;

    @Test
    public void throwsWhenAirplaneNotFound() {
        when(airplaneRepository.findById("XXXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.generateRoute("XXXX", ContinentEnum.SA))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Airplane Not Found");
    }

    @Test
    void calculatesKnownDistanceCorrectly() {
        AirportModel gru = new AirportModel("SBGR",
                "São Paulo/Guarulhos–Governor André Franco Montoro International Airport", ContinentEnum.SA, "BR",
                "São Paulo", "https://en.wikipedia.org/wiki/S%C3%A3o_Paulo-Guarulhos_International_Airport", -23.431274,
                -46.469954, AirportTypeEnum.Large);
        AirportModel jfk = new AirportModel("KJFK", "John F. Kennedy International Airport", ContinentEnum.NA, "US",
                "New York", "https://en.wikipedia.org/wiki/John_F._Kennedy_International_Airport", 40.639447,
                -73.779317, AirportTypeEnum.Large);

        int distance = routeService.calculateDistanceNm(gru, jfk);

        assertThat(distance).isCloseTo(4175, within(50));
    }

    @Test
    void calculatesKnownFlightTimeCorrectly() {
        Duration duration = routeService.calculateFlightTime(1000, 450);

        assertThat(duration).isEqualTo(Duration.ofMinutes(133));
    }

    @Test
    void throwsWhenNoArrivalAirportNearby() {
        when(airportRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> routeService.getArrivalAirport(
                new AirplaneModel("XXXX", "XXXX", 0, 0, AirplaneTypeEnum.Small),
                new AirportModel("SBGR", "São Paulo/Guarulhos–Governor André Franco Montoro International Airport",
                        ContinentEnum.SA, "BR", "São Paulo",
                        "https://en.wikipedia.org/wiki/S%C3%A3o_Paulo-Guarulhos_International_Airport", -23.431274,
                        -46.469954, AirportTypeEnum.Large)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No nearby Airport {SBGR} that support the airplane");
    }
}

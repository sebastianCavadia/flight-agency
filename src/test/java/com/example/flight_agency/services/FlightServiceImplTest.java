package com.example.flight_agency.services;


import com.example.flight_agency.api.dto.FlightDtos;
import com.example.flight_agency.domine.entities.Airline;
import com.example.flight_agency.domine.entities.Airport;
import com.example.flight_agency.domine.entities.Flight;
import com.example.flight_agency.domine.repositories.AirlineRepository;
import com.example.flight_agency.domine.repositories.AirportRepository;
import com.example.flight_agency.domine.repositories.FlightRepository;
import com.example.flight_agency.services.mappers.FlightMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private AirportRepository airportRepository;

    private final FlightMapper mapper = Mappers.getMapper(FlightMapper.class);

    @InjectMocks
    private FlightServiceImpl flightService;

    @Test
    @DisplayName("Crear vuelo")
    void createFlight() {
        Airline airline = Airline.builder().id(1L).name("Avianca").build();
        Airport origin = Airport.builder().id(1L).code("BOG").build();
        Airport destination = Airport.builder().id(2L).code("MDE").build();

        Flight flight = Flight.builder()
                .id(100L)
                .number("AV123")
                .airline(airline)
                .airport_origin(origin)
                .airport_destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        FlightDtos.FlightCreateRequest request = new FlightDtos.FlightCreateRequest(
                "AV123", flight.getDepartureTime(), flight.getArrivalTime(),
                1L, 1L, 2L
        );

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepository.findById(1L)).thenReturn(Optional.of(origin));
        when(airportRepository.findById(2L)).thenReturn(Optional.of(destination));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        FlightDtos.FlightResponse response = flightService.create(request);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.number()).isEqualTo("AV123");
        assertThat(response.origin().code()).isEqualTo("BOG");
        assertThat(response.destination().code()).isEqualTo("MDE");

        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    @DisplayName("Buscar vuelo por ID")
    void findById() {
        Flight flight = Flight.builder().id(200L).number("LA456").build();
        when(flightRepository.findById(200L)).thenReturn(Optional.of(flight));

        FlightDtos.FlightResponse response = flightService.findById(200L);

        assertThat(response.id()).isEqualTo(200L);
        assertThat(response.number()).isEqualTo("LA456");

        verify(flightRepository).findById(200L);
    }

    @Test
    @DisplayName("Buscar vuelos por aerolínea")
    void findByAirlineName() {
        Flight flight = Flight.builder().id(300L).number("IB789").build();
        when(flightRepository.findFlightByAirlineNameIgnoreCase("Iberia"))
                .thenReturn(List.of(flight));

        List<FlightDtos.FlightResponse> result = flightService.findByAirlineName("Iberia");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).number()).isEqualTo("IB789");

        verify(flightRepository).findFlightByAirlineNameIgnoreCase("Iberia");
    }

    @Test
    @DisplayName("Buscar vuelos con filtros y paginación")
    void searchFlights() {
        Flight flight = Flight.builder().id(400L).number("KL123").build();
        Page<Flight> page = new PageImpl<>(List.of(flight));

        when(flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(
                eq("BOG"), eq("MDE"),
                any(OffsetDateTime.class), any(OffsetDateTime.class),
                any(Pageable.class)
        )).thenReturn(page);

        Page<FlightDtos.FlightResponse> result = flightService.searchFlights(
                "BOG", "MDE",
                OffsetDateTime.now(), OffsetDateTime.now().plusDays(1),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).number()).isEqualTo("KL123");

        verify(flightRepository).findByOriginAndDestinationAndDepartureTimeBetween(
                eq("BOG"), eq("MDE"), any(), any(), any(Pageable.class)
        );
    }
}
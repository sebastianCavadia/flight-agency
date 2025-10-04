package com.example.flight_agency.services;


import com.example.flight_agency.api.dto.AirlineDtos;
import com.example.flight_agency.api.dto.AirportDtos;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @Mock
    private  FlightMapper mapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    private final OffsetDateTime DEPARTURE_TIME = OffsetDateTime.now();
    private final OffsetDateTime ARRIVAL_TIME = OffsetDateTime.now().plusHours(1);
    private  final Airline airline = Airline.builder().id(1L).code("AV").name("Avianca").build();
    private  final Airport origin = Airport.builder().id(1L).code("BOG").name("El Dorado").city("BOGOTÁ").build();
    private  final Airport destination = Airport.builder().id(2L).code("RAN").name("Rafael Nuñez").city("CARTAGENA").build();
    private  final AirlineDtos.AirlineResponse airlineResponse = new AirlineDtos.AirlineResponse(1L, "AV", "Avianca");
    private  final AirportDtos.AirportResponse originResponse = new AirportDtos.AirportResponse(1L, "BOG", "El Dorado", "BOGOTÁ");
    private  final AirportDtos.AirportResponse destinationResponse = new AirportDtos.AirportResponse(2L, "RAN", "Rafael Nuñez", "CARTAGENA");

    @Test
    @DisplayName("Crear vuelo")
    void createFlight() {
        Flight flight = Flight.builder()
                .number("AV123").airline(airline).airport_origin(origin).airport_destination(destination)
                .departureTime(DEPARTURE_TIME).arrivalTime(ARRIVAL_TIME).build();

        Flight savedflight = Flight.builder()
                .id(100L).number("AV123").airline(airline).airport_origin(origin).airport_destination(destination)
                .departureTime(DEPARTURE_TIME).arrivalTime(ARRIVAL_TIME).build();

        FlightDtos.FlightResponse expectResponse = new FlightDtos.FlightResponse(
                100L,"AV123",DEPARTURE_TIME,ARRIVAL_TIME,
                airlineResponse,originResponse,destinationResponse,
                Set.of()
        );
        FlightDtos.FlightCreateRequest request = new FlightDtos.FlightCreateRequest(
                "AV123",DEPARTURE_TIME, ARRIVAL_TIME, 1L, 1L, 2L
        );

        when(airlineRepository.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepository.findById(1L)).thenReturn(Optional.of(origin));
        when(airportRepository.findById(2L)).thenReturn(Optional.of(destination));
        when(mapper.toEntity(eq(request), eq(airline), eq(origin), eq(destination))).thenReturn(flight);
        when(flightRepository.save(flight)).thenReturn(savedflight);
        when(mapper.toResponse(savedflight)).thenReturn(expectResponse);

        FlightDtos.FlightResponse response = flightService.create(request);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.number()).isEqualTo("AV123");
        assertThat(response.origin().code()).isEqualTo("BOG");
        assertThat(response.destination().code()).isEqualTo("RAN");

        verify(airlineRepository).findById(1L);
        verify(airportRepository).findById(1L);
        verify(airportRepository).findById(2L);
        verify(mapper).toEntity(eq(request), eq(airline), eq(origin), eq(destination));
        verify(flightRepository).save(flight);
        verify(mapper).toResponse(savedflight);
    }

    @Test
    @DisplayName("Buscar vuelo por id")
    void findById() {
        Flight flight = Flight.builder()
                .id(200L).number("LA456")
                .departureTime(DEPARTURE_TIME).arrivalTime(ARRIVAL_TIME)
                .airline(airline)
                .airport_origin(origin)
                .airport_destination(destination)
                .build();

        FlightDtos.FlightResponse flightResponse = new FlightDtos.FlightResponse(
                200L,"LA456",DEPARTURE_TIME,ARRIVAL_TIME,
                airlineResponse, originResponse, destinationResponse, Set.of()
        );

        when(flightRepository.findById(200L)).thenReturn(Optional.of(flight));
        when(mapper.toResponse(flight)).thenReturn(flightResponse);

        FlightDtos.FlightResponse response = flightService.findById(200L);

        assertThat(response.id()).isEqualTo(200L);
        assertThat(response.number()).isEqualTo("LA456");
        assertThat(response.origin().code()).isEqualTo("BOG");

        verify(flightRepository).findById(200L);
        verify(mapper).toResponse(flight);
    }

    @Test
    @DisplayName("Buscar vuelos por aerolínea")
    void findByAirlineName() {
        String searchName = "Avianca";
        Flight flight = Flight.builder()
                .id(300L).number("IB789")
                .departureTime(DEPARTURE_TIME).arrivalTime(ARRIVAL_TIME)
                .airline(airline)
                .airport_origin(origin)
                .airport_destination(destination)
                .build();

        // CORRECCIÓN: Se usan los DTOs de soporte completos
        FlightDtos.FlightResponse flightResponse = new FlightDtos.FlightResponse(
                300L,"IB789",DEPARTURE_TIME,ARRIVAL_TIME,airlineResponse,originResponse,destinationResponse,Set.of()
        );

        when(flightRepository.findFlightByAirlineNameIgnoreCase(searchName)).thenReturn(List.of(flight));

        when(mapper.toResponse(flight)).thenReturn(flightResponse);

        List<FlightDtos.FlightResponse> result = flightService.findByAirlineName(searchName);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).number()).isEqualTo("IB789");
        assertThat(result.get(0).airline().name()).isEqualTo(searchName);

        verify(flightRepository).findFlightByAirlineNameIgnoreCase(searchName);
        verify(mapper).toResponse(flight);
    }


    @Test
    @DisplayName("Buscar vuelos con filtros y paginación")
    void searchFlights() {
        Pageable pageable = PageRequest.of(0, 10);

        Flight flight = Flight.builder().id(400L).number("KL123")
                .airline(airline).airport_origin(origin).airport_destination(destination).build();
        Page<Flight> page = new PageImpl<>(List.of(flight), pageable, 1);

        FlightDtos.FlightResponse flightResponse = new FlightDtos.FlightResponse(
                400L, "KL123", DEPARTURE_TIME, ARRIVAL_TIME,
                airlineResponse, originResponse, destinationResponse, Set.of()
        );

        when(flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(
                eq(origin.getCode()), // "BOG"
                eq(destination.getCode()), // "RAN"
                any(OffsetDateTime.class),
                any(OffsetDateTime.class),
                eq(pageable)
        )).thenReturn(page);

        when(mapper.toResponse(flight)).thenReturn(flightResponse);

        Page<FlightDtos.FlightResponse> result = flightService.searchFlights(
                origin.getCode(),
                destination.getCode(),
                DEPARTURE_TIME, ARRIVAL_TIME.plusDays(1),
                pageable
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).number()).isEqualTo("KL123");
        assertThat(result.getContent().get(0).origin().code()).isEqualTo("BOG");

        verify(flightRepository).findByOriginAndDestinationAndDepartureTimeBetween(eq(origin.getCode()), eq(destination.getCode()),
                any(), any(), eq(pageable)
        );
        verify(mapper).toResponse(flight);
    }
}
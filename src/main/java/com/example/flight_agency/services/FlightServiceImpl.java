package com.example.flight_agency.services;

 import com.example.flight_agency.api.dto.FlightDtos;
 import com.example.flight_agency.domine.entities.Airline;
 import com.example.flight_agency.domine.entities.Airport;
 import com.example.flight_agency.domine.entities.Flight;
 import com.example.flight_agency.domine.repositories.AirlineRepository;
 import com.example.flight_agency.domine.repositories.AirportRepository;
 import com.example.flight_agency.domine.repositories.FlightRepository;
 import com.example.flight_agency.services.mappers.FlightMapper;
 import lombok.RequiredArgsConstructor;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.stereotype.Service;

 import java.time.OffsetDateTime;
 import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final FlightMapper mapper;

    @Override
    public FlightDtos.FlightResponse create(FlightDtos.FlightCreateRequest request) {
        Airline airline = airlineRepository.findById(request.airlineId())
                .orElseThrow(() -> new RuntimeException("Airline not found"));
        Airport origin = airportRepository.findById(request.originAirportId())
                .orElseThrow(() -> new RuntimeException("Origin not found"));
        Airport destination = airportRepository.findById(request.destinationAirportId())
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        Flight flight = mapper.toEntity(request, airline, origin, destination);
        Flight saved = flightRepository.save(flight);
        return mapper.toResponse(saved);
    }

    @Override
    public FlightDtos.FlightResponse findById(Long id) {
        return flightRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Flight no encontrado"));
    }

    @Override
    public List<FlightDtos.FlightResponse> findByAirlineName(String airlineName) {
        return flightRepository.findFlightByAirlineNameIgnoreCase(airlineName)
                .stream().map(mapper::toResponse).toList();
    }

    @Override
    public Page<FlightDtos.FlightResponse> searchFlights(String origin, String destination,
                                                         OffsetDateTime fechaInicio,
                                                         OffsetDateTime fechaSalida,
                                                         Pageable pageable) {
        return flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(
                origin, destination, fechaInicio, fechaSalida, pageable
        ).map(mapper::toResponse);
    }
}
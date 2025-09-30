package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.FlightDtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface FlightService {
    FlightDtos.FlightResponse create(FlightDtos.FlightCreateRequest request);
    FlightDtos.FlightResponse findById(Long id);
    List<FlightDtos.FlightResponse> findByAirlineName(String airlineName);
    Page<FlightDtos.FlightResponse> searchFlights(
            String origin, String destination,
            OffsetDateTime fechaInicio, OffsetDateTime fechaSalida,
            Pageable pageable);
}
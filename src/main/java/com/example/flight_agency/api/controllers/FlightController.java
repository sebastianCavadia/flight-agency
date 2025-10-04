package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.FlightDtos;
import com.example.flight_agency.services.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService service;

    @PostMapping
    public ResponseEntity<FlightDtos.FlightResponse> create(@RequestBody FlightDtos.FlightCreateRequest req) {
        return ResponseEntity.status(201).body(service.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDtos.FlightResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-airline")
    public ResponseEntity<List<FlightDtos.FlightResponse>> findByAirline(@RequestParam String airlineName) {
        return ResponseEntity.ok(service.findByAirlineName(airlineName));
    }

    // Search with origin, destination, fechaInicio and fechaSalida (ISO-8601 strings) + pageable
    @GetMapping("/search")
    public ResponseEntity<Page<FlightDtos.FlightResponse>> search(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String fechaInicio,
            @RequestParam String fechaSalida,
            Pageable pageable
    ) {
        OffsetDateTime from;
        OffsetDateTime to;
            from = OffsetDateTime.parse(fechaInicio);
            to = OffsetDateTime.parse(fechaSalida);
        return ResponseEntity.ok(service.searchFlights(origin, destination, from, to, pageable));
    }
}

package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.AirlineDtos;
import com.example.flight_agency.services.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/airlines")
@RequiredArgsConstructor
public class AirlineController {

    private final AirlineService service;

    @PostMapping
    public ResponseEntity<AirlineDtos.AirlineResponse> create(@RequestBody AirlineDtos.AirlineCreateRequest req) {
        var res = service.create(req);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineDtos.AirlineResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<AirlineDtos.AirlineResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getByCode(code));
    }
}

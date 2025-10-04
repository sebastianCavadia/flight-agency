package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.services.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService service;

    @PostMapping
    public ResponseEntity<AirportDtos.AirportResponse> create(@RequestBody AirportDtos.AirportCreateRequest req) {
        var res = service.create(req);
        return ResponseEntity.status(201).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportDtos.AirportResponse> update(@PathVariable Long id,
                                                              @RequestBody AirportDtos.AirportUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportDtos.AirportResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-code/{code}")
    public ResponseEntity<AirportDtos.AirportResponse> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<AirportDtos.AirportResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

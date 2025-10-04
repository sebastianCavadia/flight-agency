package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.SeatInventoryDtos;
import com.example.flight_agency.domine.entities.Cabin;
import com.example.flight_agency.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seat-inventories")
@RequiredArgsConstructor
public class SeatInventoryController {

    private final SeatInventoryService service;

    @PostMapping
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> create(@RequestBody SeatInventoryDtos.SeatInventoryCreateRequest req) {
        return ResponseEntity.status(201).body(service.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-flight")
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> findByFlightAndCabin(@RequestParam Long flightId, @RequestParam Cabin cabin) {
        return ResponseEntity.ok(service.findByFlightAndCabin(flightId, cabin));
    }

    @GetMapping("/has-min")
    public ResponseEntity<Boolean> hasMin(@RequestParam Long flightId, @RequestParam Cabin cabin, @RequestParam int min) {
        return ResponseEntity.ok(service.hasMinimumSeatsAvailable(flightId, cabin, min));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Boolean> reserve(@RequestParam Long flightId, @RequestParam Cabin cabin, @RequestParam int count) {
        return ResponseEntity.ok(service.reserveSeats(flightId, cabin, count));
    }

    @PostMapping("/release")
    public ResponseEntity<Void> release(@RequestParam Long flightId, @RequestParam Cabin cabin, @RequestParam int count) {
        service.releaseSeats(flightId, cabin, count);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatInventoryDtos.SeatInventoryResponse> update(@PathVariable Long id, @RequestBody SeatInventoryDtos.SeatInventoryUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

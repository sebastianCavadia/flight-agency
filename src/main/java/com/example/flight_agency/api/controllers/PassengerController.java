package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.services.PassegerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassegerService service;

    @PostMapping
    public ResponseEntity<PassengerDtos.PassengerResponse> create(@RequestBody PassengerDtos.PassengerCreateRequest req) {
        return ResponseEntity.status(201).body(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerDtos.PassengerResponse> update(@PathVariable Long id, @RequestBody PassengerDtos.PassengerUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDtos.PassengerResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-email")
    public ResponseEntity<PassengerDtos.PassengerResponse> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<PassengerDtos.PassengerResponseBasic>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }
}

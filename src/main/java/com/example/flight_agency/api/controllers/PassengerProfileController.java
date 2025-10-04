package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.PassengerProfileDtos;
import com.example.flight_agency.services.PassengerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passenger-profiles")
@RequiredArgsConstructor
public class PassengerProfileController {

    private final PassengerProfileService service;

    @PostMapping
    public ResponseEntity<PassengerProfileDtos.PassengerProfileResponse> create(@RequestBody PassengerProfileDtos.PassengerProfileCreateRequest req) {
        return ResponseEntity.status(201).body(service.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerProfileDtos.PassengerProfileResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerProfileDtos.PassengerProfileResponse> update(@PathVariable Long id,
                                                                                @RequestBody PassengerProfileDtos.PassengerProfileUpdateRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

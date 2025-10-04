package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.BookingDtos;
import com.example.flight_agency.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public ResponseEntity<BookingDtos.BookingResponse> create(@RequestBody BookingDtos.BookingCreateRequest req) {
        var res = service.create(req);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDtos.BookingResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<BookingDtos.BookingResponse>> findByPassengerEmail(
            @RequestParam(required = false) String email,
            Pageable pageable) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.ok(Page.empty(pageable));
        }
        return ResponseEntity.ok(service.findByPassengerEmail(email, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

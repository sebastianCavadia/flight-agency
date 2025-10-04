package com.example.flight_agency.api.controllers;

import com.example.flight_agency.api.dto.BookingItemDtos;
import com.example.flight_agency.services.BookingItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking-items")
@RequiredArgsConstructor
public class BookingItemController {

    private final BookingItemService service;

    @PostMapping
    public ResponseEntity<BookingItemDtos.BookingItemResponse> create(@RequestBody BookingItemDtos.BookingItemCreateRequest req) {
        var res = service.create(req);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingItemDtos.BookingItemResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingItemDtos.BookingItemResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

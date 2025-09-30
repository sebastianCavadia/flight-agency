package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.BookingItemDtos;

import java.util.List;

public interface BookingItemService {
    BookingItemDtos.BookingItemResponse create(BookingItemDtos.BookingItemCreateRequest request);
    BookingItemDtos.BookingItemResponse findById(Long id);
    List<BookingItemDtos.BookingItemResponse> findAll();
    void delete(Long id);
}
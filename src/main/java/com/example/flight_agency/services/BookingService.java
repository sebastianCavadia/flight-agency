package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.BookingDtos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingDtos.BookingResponse create(BookingDtos.BookingCreateRequest request);
    BookingDtos.BookingResponse findById(Long id);
    Page<BookingDtos.BookingResponse> findByPassengerEmail(String email, Pageable pageable);
    void delete(Long id);
}

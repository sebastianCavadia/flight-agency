package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.BookingDtos;
import com.example.flight_agency.domine.entities.*;
import com.example.flight_agency.domine.repositories.BookingRepository;
import com.example.flight_agency.domine.repositories.FlightRepository;
import com.example.flight_agency.domine.repositories.PassengerRepository;
import com.example.flight_agency.services.mappers.BookingMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDtos.BookingResponse create(BookingDtos.BookingCreateRequest request) {
        Booking booking = bookingMapper.toEntity(request);
        Passenger passenger = passengerRepository.findById(request.passengerId())
                .orElseThrow(() -> new EntityNotFoundException("Passenger no encontrado por id " + request.passengerId()));
        booking.setPassenger(passenger);

        request.items().forEach(itemReq -> {
            Flight flight = flightRepository.findById(itemReq.flightId())
                    .orElseThrow(() -> new EntityNotFoundException("Flight no encontrado por id " + itemReq.flightId()));

            BookingItem item = BookingItem.builder()
                    .flight(flight)
                    .cabin(itemReq.cabin())
                    .segmentOrder(itemReq.segmentOrder())
                    .build();

            booking.addItem(item);
        });

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDtos.BookingResponse findById(Long id) {
        return bookingRepository.findByIdWithDetails(id)
                .map(bookingMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Booking no encontrado por id " + id));
    }

    @Override
    public Page<BookingDtos.BookingResponse> findByPassengerEmail(String email, Pageable pageable) {
        if (email == null || email.isBlank()) return Page.empty(pageable);
        return bookingRepository.findByPassengerEmailIgnoreCaseOrderByCreateAtDesc(email, pageable)
                .map(bookingMapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking no encontrado por id " + id);
        }
        bookingRepository.deleteById(id);
    }
}
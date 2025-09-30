package com.example.flight_agency.services;
import com.example.flight_agency.api.dto.BookingItemDtos;
import com.example.flight_agency.domine.entities.BookingItem;
import com.example.flight_agency.domine.entities.Flight;
import com.example.flight_agency.domine.repositories.BookingItemRepository;
import com.example.flight_agency.domine.repositories.FlightRepository;
import com.example.flight_agency.services.mappers.BookingItemMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingItemServiceImpl implements BookingItemService {

    private final BookingItemRepository bookingItemRepository;
    private final FlightRepository flightRepository;
    private final BookingItemMapper bookingItemMapper;

    @Override
    public BookingItemDtos.BookingItemResponse create(BookingItemDtos.BookingItemCreateRequest request) {
        Flight flight = flightRepository.findById(request.flightId())
                .orElseThrow(() -> new EntityNotFoundException("Flight not found with id " + request.flightId()));

        BookingItem bookingItem = bookingItemMapper.toEntity(request, flight);
        BookingItem saved = bookingItemRepository.save(bookingItem);

        return bookingItemMapper.toResponse(saved);
    }

    @Override
    public BookingItemDtos.BookingItemResponse findById(Long id) {
        return bookingItemRepository.findById(id)
                .map(bookingItemMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("BookingItem not found with id " + id));
    }

    @Override
    public List<BookingItemDtos.BookingItemResponse> findAll() {
        return bookingItemRepository.findAll().stream()
                .map(bookingItemMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!bookingItemRepository.existsById(id)) {
            throw new EntityNotFoundException("BookingItem not found with id " + id);
        }
        bookingItemRepository.deleteById(id);
    }
}

package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.SeatInventoryDtos;
import com.example.flight_agency.domine.entities.*;
import com.example.flight_agency.domine.repositories.*;
import com.example.flight_agency.services.mappers.SeatInventoryMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatInventoryServiceImpl implements SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;
    private final FlightRepository flightRepository;
    private final SeatInventoryMapper seatInventoryMapper;

    @Override
    @Transactional
    public SeatInventoryDtos.SeatInventoryResponse create(SeatInventoryDtos.SeatInventoryCreateRequest req) {
        Flight flight = flightRepository.findById(req.flightId())
                .orElseThrow(() -> new EntityNotFoundException("Flight no encontrado"));

        SeatInventory entity = SeatInventory.builder()
                .flight(flight)
                .cabin(req.cabin())
                .totalSeats(req.totalSeats())
                .availableSeats(req.availableSeats())
                .build();

        return seatInventoryMapper.toResponse(seatInventoryRepository.save(entity));
    }

    @Override
    public SeatInventoryDtos.SeatInventoryResponse findById(Long id) {
        return seatInventoryRepository.findById(id).map(seatInventoryMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("SeatInventory no encontrado"));
    }

    @Override
    public SeatInventoryDtos.SeatInventoryResponse findByFlightAndCabin(Long flightId, Cabin cabin) {
        return seatInventoryRepository.findByFlightIdAndCabin(flightId, cabin)
                .map(seatInventoryMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("SeatInventory no encontrado"));
    }

    @Override
    public boolean hasMinimumSeatsAvailable(Long flightId, Cabin cabin, int min) {
        return seatInventoryRepository.hasMinimumSeatsAvailable(flightId, cabin, min);
    }

    @Override
    @Transactional
    public boolean reserveSeats(Long flightId, Cabin cabin, int count) {
        if (count <= 0) throw new IllegalArgumentException("La cantidad debe ser > 0");
        return seatInventoryRepository.decrementAvailableSeatsIfEnough(flightId, cabin, count) > 0;
    }

    @Override
    @Transactional
    public void releaseSeats(Long flightId, Cabin cabin, int count) {
        if (count <= 0) throw new IllegalArgumentException("La cantidad debe ser > 0");
        seatInventoryRepository.incrementAvailableSeats(flightId, cabin, count);
    }

    @Override
    @Transactional
    public SeatInventoryDtos.SeatInventoryResponse update(Long id, SeatInventoryDtos.SeatInventoryUpdateRequest req) {
        SeatInventory entity = seatInventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SeatInventory no encontrado"));
        entity.setTotalSeats(req.totalSeats());
        entity.setAvailableSeats(req.availableSeats());
        return seatInventoryMapper.toResponse(seatInventoryRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        seatInventoryRepository.deleteById(id);
    }
}

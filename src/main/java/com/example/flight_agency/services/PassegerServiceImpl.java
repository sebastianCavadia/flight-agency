package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.domine.entities.Passenger;
import com.example.flight_agency.domine.repositories.PassengerRepository;
import com.example.flight_agency.services.mappers.PassengerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassegerServiceImpl implements PassegerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper mapper;

    @Override
    public PassengerDtos.PassengerResponse create(PassengerDtos.PassengerCreateRequest request) {
        Passenger passenger = mapper.toEntity(request);
        Passenger saved = passengerRepository.save(passenger);
        return mapper.toResponse(saved);
    }

    @Override
    public PassengerDtos.PassengerResponse update(Long id, PassengerDtos.PassengerUpdateRequest request) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        passenger.setFullName(request.fullName());
        passenger.setEmail(request.email());
        passenger.setPassengerProfile(mapper.toEntity(request.profile()));

        Passenger updated = passengerRepository.save(passenger);
        return mapper.toResponse(updated);
    }

    @Override
    public PassengerDtos.PassengerResponse findById(Long id) {
        return passengerRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
    }

    @Override
    public PassengerDtos.PassengerResponse findByEmail(String email) {
        return passengerRepository.findPassengerByEmailIgnoreCase(email)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
    }

    @Override
    public List<PassengerDtos.PassengerResponseBasic> findAll() {
        return passengerRepository.findAll().stream()
                .map(mapper::toResponseBasic)
                .toList();
    }
}
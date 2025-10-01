package com.example.flight_agency.services;
import com.example.flight_agency.api.dto.AirlineDtos;
import com.example.flight_agency.services.mappers.*;
import com.example.flight_agency.domine.entities.Airline;
import com.example.flight_agency.domine.repositories.AirlineRepository;
import com.example.flight_agency.services.mappers.AirlineMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;

    public AirlineServiceImpl(AirlineRepository airlineRepository, AirlineMapper airlineMapper) {
        this.airlineRepository = airlineRepository;
        this.airlineMapper = airlineMapper;
    }

    @Override
    public AirlineDtos.AirlineResponse create(AirlineDtos.AirlineCreateRequest request) {
        Airline airline = airlineMapper.toEntity(request);
        Airline saved = airlineRepository.save(airline);
        return airlineMapper.toResponse(saved);
    }

    @Override
    public AirlineDtos.AirlineResponse getByCode(String code) {
        return airlineRepository.findAirlineByCodeIgnoreCase(code)
                .map(airlineMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Airline no encontado por codigo: " + code));
    }

    @Override
    public AirlineDtos.AirlineResponse getById(Long id) {
        return airlineRepository.findById(id)
                .map(airlineMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Airline no encontrado por id: " + id));
    }
}
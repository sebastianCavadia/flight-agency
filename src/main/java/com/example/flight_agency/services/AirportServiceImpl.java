package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.domine.entities.Airport;
import com.example.flight_agency.domine.repositories.AirportRepository;
import com.example.flight_agency.services.mappers.AirportMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    @Override
    public AirportDtos.AirportResponse create(AirportDtos.AirportCreateRequest request) {
        Airport airport = airportMapper.toEntity(request);
        Airport saved = airportRepository.save(airport);
        return airportMapper.toResponse(saved);
    }

    @Override
    public AirportDtos.AirportResponse update(Long id, AirportDtos.AirportUpdateRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Airport no encontrado por id " + id));
        airportMapper.updateEntityFromRequest(request, airport);
        return airportMapper.toResponse(airportRepository.save(airport));
    }

    @Override
    public AirportDtos.AirportResponse findById(Long id) {
        return airportRepository.findById(id)
                .map(airportMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Airport no encontrado por id " + id));
    }

    @Override
    public AirportDtos.AirportResponse findByCode(String code) {
        return airportRepository.findByCode(code)
                .map(airportMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Airport no encontrado por codigo " + code));
    }

    @Override
    public List<AirportDtos.AirportResponse> findAll() {
        return airportRepository.findAll().stream()
                .map(airportMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!airportRepository.existsById(id)) {
            throw new EntityNotFoundException("Airport no encontrado por id " + id);
        }
        airportRepository.deleteById(id);
    }
}

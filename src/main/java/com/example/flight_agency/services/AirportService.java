package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.AirportDtos;

import java.util.List;

public interface AirportService {

    AirportDtos.AirportResponse create(AirportDtos.AirportCreateRequest request);
    AirportDtos.AirportResponse update(Long id, AirportDtos.AirportUpdateRequest request);
    AirportDtos.AirportResponse findById(Long id);
    AirportDtos.AirportResponse findByCode(String code);
    List<AirportDtos.AirportResponse> findAll();

    void delete(Long id);
}

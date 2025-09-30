package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.AirlineDtos;

public interface AirlineService {
    AirlineDtos.AirlineResponse create(AirlineDtos.AirlineCreateRequest request);
    AirlineDtos.AirlineResponse getByCode(String code);
    AirlineDtos.AirlineResponse getById(Long id);
}

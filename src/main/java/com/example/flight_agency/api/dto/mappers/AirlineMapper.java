package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.AirlineDtos;
import com.example.flight_agency.domine.entities.Airline;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirlineMapper {
    AirlineMapper INSTANCE = Mappers.getMapper(AirlineMapper.class);
    AirlineDtos.AirlineResponse toResponse(Airline airline);
    Airline toEntity(AirlineDtos.AirlineCreateRequest request);
}

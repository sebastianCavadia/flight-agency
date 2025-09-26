package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.domine.entities.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirportMapper {
    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    AirportDtos.AirportResponse toResponse(Airport airport);

    Airport toEntity(AirportDtos.AirportCreateRequest request);
}

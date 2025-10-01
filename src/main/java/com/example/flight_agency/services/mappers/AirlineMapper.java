package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.AirlineDtos;
import com.example.flight_agency.domine.entities.Airline;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AirlineMapper {
    AirlineDtos.AirlineResponse toResponse(Airline airline);
    Airline toEntity(AirlineDtos.AirlineCreateRequest request);

    // Para actualizar parcialmente
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AirlineDtos.AirlineCreateRequest request, @MappingTarget Airline entity);
}

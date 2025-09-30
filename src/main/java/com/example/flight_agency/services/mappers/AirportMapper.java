package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.AirportDtos;
import com.example.flight_agency.domine.entities.Airport;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    AirportDtos.AirportResponse toResponse(Airport airport);

    Airport toEntity(AirportDtos.AirportCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AirportDtos.AirportUpdateRequest request, @MappingTarget Airport entity);
}

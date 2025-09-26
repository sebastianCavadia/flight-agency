package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.domine.entities.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassengerMapper {
    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);

    PassengerDtos.PassengerResponse toResponse(Passenger passenger);

    Passenger toEntity(PassengerDtos.PassengerCreateRequest request);
}

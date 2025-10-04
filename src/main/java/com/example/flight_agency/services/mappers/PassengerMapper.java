package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.domine.entities.Passenger;
import com.example.flight_agency.domine.entities.PassengerProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    PassengerDtos.PassengerResponse toResponse(Passenger passenger);
    PassengerDtos.PassengerResponseBasic toResponseBasic(Passenger passenger);
    Passenger toEntity(PassengerDtos.PassengerCreateRequest request);

    PassengerDtos.PassengerProfileDto toDto(PassengerProfile profile);
    PassengerProfile toEntity(PassengerDtos.PassengerProfileDto dto);
}

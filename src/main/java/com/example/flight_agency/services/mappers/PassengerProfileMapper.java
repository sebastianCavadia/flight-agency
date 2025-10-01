package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.PassengerProfileDtos;
import com.example.flight_agency.domine.entities.PassengerProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerProfileMapper {
    PassengerProfileDtos.PassengerProfileResponse toResponse(PassengerProfile profile);
    PassengerProfile toEntity(PassengerProfileDtos.PassengerProfileCreateRequest request);
}

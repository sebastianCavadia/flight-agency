package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.api.dto.PassengerProfileDTO;
import com.example.flight_agency.domine.entities.PassengerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PassengerProfileMapper {
    PassengerProfileMapper INSTANCE = Mappers.getMapper(PassengerProfileMapper.class);
    PassengerProfileDTO.PassengerProfileResponse toResponse(PassengerProfile profile);
    PassengerProfile toEntity(PassengerProfileDTO.PassengerProfileCreateRequest request);
}

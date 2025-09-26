package com.example.flight_agency.api.dto.mappers;

import com.example.flight_agency.api.dto.FlightDtos;
import com.example.flight_agency.domine.entities.Airline;
import com.example.flight_agency.domine.entities.Airport;
import com.example.flight_agency.domine.entities.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {
    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);
    FlightDtos.FlightResponse toResponse(Flight flight);
    Flight toEntity(FlightDtos.FlightCreateRequest request, Airline airline, Airport originAirport, Airport destinationAirport);
}

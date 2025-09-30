package com.example.flight_agency.services.mappers;

import com.example.flight_agency.api.dto.FlightDtos;
import com.example.flight_agency.domine.entities.Airline;
import com.example.flight_agency.domine.entities.Airport;
import com.example.flight_agency.domine.entities.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    FlightDtos.FlightResponse toResponse(Flight flight);
    Flight toEntity(FlightDtos.FlightCreateRequest request, Airline airline, Airport originAirport, Airport destinationAirport);
}

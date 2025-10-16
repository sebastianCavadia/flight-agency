package com.example.flight_agency.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;
public class FlightDtos {
    public record FlightCreateRequest(
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            Long airlineId,
            Long originAirportId,
            Long destinationAirportId
    ) implements Serializable {}
    public record FlightResponseBasic(
            Long id,
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime
    ) implements Serializable {}
    public record FlightResponse(
            Long id,
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,

            AirlineDtos.AirlineResponse airline,
            AirportDtos.AirportResponse origin,
            AirportDtos.AirportResponse destination,
            Set<TagDtos.TagResponse> tags
    ) implements Serializable {}
}

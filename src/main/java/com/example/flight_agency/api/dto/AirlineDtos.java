package com.example.flight_agency.api.dto;
import java.io.Serializable;

public class AirlineDtos {
    public record AirlineCreateRequest(String code, String name) implements Serializable {}
    public record AirlineUpdateRequest(String name) implements Serializable {}
    public record AirlineResponse(Long id, String code, String name) implements Serializable {}
}

package com.example.flight_agency.api.dto;

import java.io.Serializable;

public class PassengerProfileDTO {
    public record PassengerProfileCreateRequest(String phone, String countryCode) implements Serializable {}

    public record PassengerProfileUpdateRequest(String phone, String countryCode) implements Serializable {}

    public record PassengerProfileResponse(Long id, String phone, String countryCode) implements Serializable {}
}

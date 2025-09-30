package com.example.flight_agency.api.dto;
import java.io.Serializable;
import java.util.List;

public class PassengerDtos {
    public record PassengerCreateRequest(String fullName, String email, PassengerProfileDto profile) implements Serializable {}
    public record PassengerUpdateRequest(String fullName, String email, PassengerProfileDto profile) implements Serializable {}
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable {}
    public record PassengerResponseBasic(Long id, String fullName, String email) implements Serializable {}
    public record PassengerResponse(Long id, String fullName, String email, PassengerProfileDto profile, List<BookingDtos.BookingResponse> bookings) implements Serializable {}
}

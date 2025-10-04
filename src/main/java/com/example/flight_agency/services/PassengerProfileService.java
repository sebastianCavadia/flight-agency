package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.PassengerProfileDtos;

public interface PassengerProfileService {
    PassengerProfileDtos.PassengerProfileResponse create(PassengerProfileDtos.PassengerProfileCreateRequest request);
    PassengerProfileDtos.PassengerProfileResponse update(Long id, PassengerProfileDtos.PassengerProfileUpdateRequest request);
    PassengerProfileDtos.PassengerProfileResponse findById(Long id);
    void delete(Long id);
}

package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.PassengerDtos;

import java.util.List;

public interface PassegerService {
    PassengerDtos.PassengerResponse create(PassengerDtos.PassengerCreateRequest request);
    PassengerDtos.PassengerResponse update(Long id, PassengerDtos.PassengerUpdateRequest request);
    PassengerDtos.PassengerResponse findById(Long id);
    PassengerDtos.PassengerResponse findByEmail(String email);
    List<PassengerDtos.PassengerResponseBasic> findAll();
}
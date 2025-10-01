package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.PassengerProfileDtos;
import com.example.flight_agency.domine.entities.PassengerProfile;
import com.example.flight_agency.domine.repositories.PassengerProfileRepository;
import com.example.flight_agency.services.mappers.PassengerProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerProfileServiceImpl implements PassengerProfileService {

    private final PassengerProfileRepository repository;
    private final PassengerProfileMapper mapper;

    @Override
    public PassengerProfileDtos.PassengerProfileResponse create(PassengerProfileDtos.PassengerProfileCreateRequest request) {
        PassengerProfile profile = mapper.toEntity(request);
        PassengerProfile saved = repository.save(profile);
        return mapper.toResponse(saved);
    }

    @Override
    public PassengerProfileDtos.PassengerProfileResponse update(Long id, PassengerProfileDtos.PassengerProfileUpdateRequest request) {
        PassengerProfile existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile no encontrado"));
        existing.setPhone(request.phone());
        existing.setCountryCode(request.countryCode());
        PassengerProfile updated = repository.save(existing);
        return mapper.toResponse(updated);
    }

    @Override
    public PassengerProfileDtos.PassengerProfileResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Profile no encontrado"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

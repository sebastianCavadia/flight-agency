package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.PassengerProfileDtos;
import com.example.flight_agency.domine.entities.PassengerProfile;
import com.example.flight_agency.domine.repositories.PassengerProfileRepository;
import com.example.flight_agency.services.mappers.PassengerProfileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerProfileServiceImplTest {

    @Mock
    private PassengerProfileRepository repository;

    @Mock
    private PassengerProfileMapper mapper;

    @InjectMocks
    private PassengerProfileServiceImpl service;

    private PassengerProfile profile;
    private PassengerProfileDtos.PassengerProfileCreateRequest createRequest;
    private PassengerProfileDtos.PassengerProfileResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        profile = PassengerProfile.builder()
                .id(1L)
                .phone("3225887711")
                .countryCode("+57")
                .build();

        createRequest = new PassengerProfileDtos.PassengerProfileCreateRequest("3225887711", "+57");
        response = new PassengerProfileDtos.PassengerProfileResponse(1L, "3225887711", "+57");
    }

    @Test
    void createProfile() {
        when(mapper.toEntity(createRequest)).thenReturn(profile);
        when(repository.save(any(PassengerProfile.class))).thenReturn(profile);
        when(mapper.toResponse(profile)).thenReturn(response);

        PassengerProfileDtos.PassengerProfileResponse result = service.create(createRequest);

        assertThat(result.id()).isEqualTo(1L);
        verify(repository).save(profile);
    }

    @Test
    void findById_found() {
        when(repository.findById(1L)).thenReturn(Optional.of(profile));
        when(mapper.toResponse(profile)).thenReturn(response);

        var result = service.findById(1L);

        assertThat(result.phone()).isEqualTo("3225887711");
        verify(repository).findById(1L);
    }

    @Test
    void findById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(1L));
    }

    @Test
    void updateProfile() {
        PassengerProfileDtos.PassengerProfileUpdateRequest updateReq =
                new PassengerProfileDtos.PassengerProfileUpdateRequest("987654", "+1");

        when(repository.findById(1L)).thenReturn(Optional.of(profile));
        when(repository.save(any(PassengerProfile.class))).thenReturn(profile);
        when(mapper.toResponse(profile)).thenReturn(
                new PassengerProfileDtos.PassengerProfileResponse(1L, "987654", "+1")
        );

        var result = service.update(1L, updateReq);

        assertThat(result.phone()).isEqualTo("987654");
        verify(repository).save(profile);
    }

    @Test
    void deleteProfile() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}
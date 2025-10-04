package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.domine.entities.Passenger;
import com.example.flight_agency.domine.entities.PassengerProfile;
import com.example.flight_agency.domine.repositories.PassengerRepository;
import com.example.flight_agency.services.mappers.PassengerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassegerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper mapper;

    @InjectMocks
    private PassegerServiceImpl passengerService;

    @Test
    @DisplayName("Crear pasajero")
    void createPassenger() {
        PassengerDtos.PassengerCreateRequest request = new PassengerDtos.PassengerCreateRequest(
                "Juan Pérez", "juan@test.com",
                new PassengerDtos.PassengerProfileDto("123456", "+57")
        );

        Passenger passenger = Passenger.builder()
                .fullName("Juan Pérez")
                .email("juan@test.com")
                .passengerProfile(PassengerProfile.builder().phone("123456").countryCode("+57").build())
                .build();

        Passenger passengerSaved = Passenger.builder()
                .id(1L)
                .fullName("Juan Pérez")
                .email("juan@test.com")
                .passengerProfile(PassengerProfile.builder().phone("123456").countryCode("+57").build())
                .build();
        PassengerDtos.PassengerResponse expectedResponse = new PassengerDtos.PassengerResponse(
                1L, "Juan Pérez", "juan@test.com",
                new PassengerDtos.PassengerProfileDto("123456", "+57"),
                List.of()
        );
        when(mapper.toEntity(request)).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passengerSaved);
        when(mapper.toResponse(passengerSaved)).thenReturn(expectedResponse);

        PassengerDtos.PassengerResponse response = passengerService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.fullName()).isEqualTo("Juan Pérez");
        assertThat(response.profile().phone()).isEqualTo("123456");

        verify(mapper).toEntity(request);
        verify(passengerRepository).save(passenger);
        verify(mapper).toResponse(passengerSaved);
    }

    @Test
    @DisplayName("Buscar pasajero por email")
    void findByEmail() {
        Passenger passenger = Passenger.builder()
                .id(2L)
                .fullName("Maria Lopez")
                .email("maria@test.com")
                .build();
        PassengerDtos.PassengerResponse expectedResponse = new PassengerDtos.PassengerResponse(
                2L, "Maria Lopez", "maria@test.com", null, List.of()
        );

        when(passengerRepository.findPassengerByEmailIgnoreCase("maria@test.com"))
                .thenReturn(Optional.of(passenger));
        when(mapper.toResponse(passenger)).thenReturn(expectedResponse);

        PassengerDtos.PassengerResponse response = passengerService.findByEmail("maria@test.com");

        assertThat(response.email()).isEqualTo("maria@test.com");
        assertThat(response.fullName()).isEqualTo("Maria Lopez");

        verify(passengerRepository).findPassengerByEmailIgnoreCase("maria@test.com");
        verify(mapper).toResponse(passenger);
    }

    @Test
    @DisplayName("Listar pasajeros")
    void listPassengers() {
        Passenger p1 = Passenger.builder().id(1L).fullName("User 1").email("u1@test.com").build();
        Passenger p2 = Passenger.builder().id(2L).fullName("User 2").email("u2@test.com").build();

        PassengerDtos.PassengerResponseBasic r1 = new PassengerDtos.PassengerResponseBasic(1L, "User 1", "u1@test.com");
        PassengerDtos.PassengerResponseBasic r2 = new PassengerDtos.PassengerResponseBasic(2L, "User 2", "u2@test.com");

        when(passengerRepository.findAll()).thenReturn(List.of(p1, p2));
        when(mapper.toResponseBasic(p1)).thenReturn(r1);
        when(mapper.toResponseBasic(p2)).thenReturn(r2);

        List<PassengerDtos.PassengerResponseBasic> list = passengerService.findAll();

        assertThat(list).hasSize(2);
        assertThat(list.getFirst().email()).isEqualTo("u1@test.com");

        verify(passengerRepository).findAll();
        verify(mapper).toResponseBasic(p1);
        verify(mapper).toResponseBasic(p2);
    }
}
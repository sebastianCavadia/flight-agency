package com.example.flight_agency.services;

import com.example.flight_agency.api.dto.PassengerDtos;
import com.example.flight_agency.domine.entities.Passenger;
import com.example.flight_agency.domine.entities.PassengerProfile;
import com.example.flight_agency.domine.repositories.PassengerRepository;
import com.example.flight_agency.services.mappers.PassengerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassegerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    private final PassengerMapper mapper = Mappers.getMapper(PassengerMapper.class);

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
                .id(1L)
                .fullName("Juan Pérez")
                .email("juan@test.com")
                .passengerProfile(PassengerProfile.builder().phone("123456").countryCode("+57").build())
                .build();

        when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

        PassengerDtos.PassengerResponse response = passengerService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.fullName()).isEqualTo("Juan Pérez");
        assertThat(response.profile().phone()).isEqualTo("123456");

        verify(passengerRepository).save(any(Passenger.class));
    }

    @Test
    @DisplayName("Buscar pasajero por email")
    void findByEmail() {
        Passenger passenger = Passenger.builder()
                .id(2L)
                .fullName("Maria Lopez")
                .email("maria@test.com")
                .build();

        when(passengerRepository.findPassengerByEmailIgnoreCase("maria@test.com"))
                .thenReturn(Optional.of(passenger));

        PassengerDtos.PassengerResponse response = passengerService.findByEmail("maria@test.com");

        assertThat(response.email()).isEqualTo("maria@test.com");
        assertThat(response.fullName()).isEqualTo("Maria Lopez");

        verify(passengerRepository).findPassengerByEmailIgnoreCase("maria@test.com");
    }

    @Test
    @DisplayName("Listar pasajeros")
    void listPassengers() {
        Passenger p1 = Passenger.builder().id(1L).fullName("User 1").email("u1@test.com").build();
        Passenger p2 = Passenger.builder().id(2L).fullName("User 2").email("u2@test.com").build();

        when(passengerRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PassengerDtos.PassengerResponseBasic> list = passengerService.findAll();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).email()).isEqualTo("u1@test.com");

        verify(passengerRepository).findAll();
    }
}
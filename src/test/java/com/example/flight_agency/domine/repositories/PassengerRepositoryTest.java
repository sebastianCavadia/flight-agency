package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.Passenger;
import com.example.flight_agency.domine.entities.PassengerProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PassengerRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private PassengerProfileRepository passengerProfileRepository;

    @Test
    @DisplayName("Buscar por email")
    void findByEmailIgnoreCase() {
        PassengerProfile profile = PassengerProfile.builder()
                .phone("3226955656")
                .countryCode("+57")
                .build();
        Passenger p = Passenger.builder().fullName("Juan carlos Gacha").
                email("gacha12Juan@gmail.com").passengerProfile(profile).build();
        passengerRepository.save(p);

        Optional<Passenger> found = passengerRepository.findPassengerByEmailIgnoreCase("gacha12Juan@gmail.com");

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(p);
    }

    @Test
    @DisplayName("Buscar perfil por email")
    void findPassengerByEmail() {
        PassengerProfile pp = PassengerProfile.builder().phone("3226778899").
                countryCode("+57").build();

        Passenger p = Passenger.builder().fullName("Carlos Garra Gomez").
                email("gomezcarlos@gmail.com").passengerProfile(pp).build();
        passengerRepository.save(p);

        Optional<Passenger> found = passengerRepository.findPassengerByEmailIgnoreCase(p.getEmail());

        PassengerProfile foundProfile = passengerProfileRepository.findById(pp.getId()).orElseThrow();
        assertThat(found).isPresent();
        assertThat(found.get().getPassengerProfile()).isEqualTo(p.getPassengerProfile());

        assertThat(foundProfile.getPassenger()).isNotNull();
        assertThat(foundProfile.getPassenger().getEmail()).isEqualTo("gomezcarlos@gmail.com");

    }
}
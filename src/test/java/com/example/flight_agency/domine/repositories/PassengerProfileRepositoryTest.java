package com.example.flight_agency.domine.repositories;


import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.PassengerProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
class PassengerProfileRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private PassengerProfileRepository passengerProfileRepository;

    @Test
    @DisplayName("Guardar y recuperar PassengerProfile")
    void saveAndFindPassengerProfile() {
        PassengerProfile profile = PassengerProfile.builder()
                .phone("3226223355")
                .countryCode("+57")
                .build();

        PassengerProfile saved = passengerProfileRepository.save(profile);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPhone()).isEqualTo("3226223355");
        assertThat(saved.getCountryCode()).isEqualTo("+57");

        PassengerProfile found = passengerProfileRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getPhone()).isEqualTo("3226223355");
        assertThat(found.getCountryCode()).isEqualTo("+57");
    }

}
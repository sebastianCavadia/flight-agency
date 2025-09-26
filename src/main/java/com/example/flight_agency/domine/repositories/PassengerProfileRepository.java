package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.PassengerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, Long> {

}

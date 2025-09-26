package com.example.flight_agency.domine.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "passengerProfiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerProfile {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String countryCode;

    @OneToOne(mappedBy = "passengerProfile")
    private Passenger passenger;
}



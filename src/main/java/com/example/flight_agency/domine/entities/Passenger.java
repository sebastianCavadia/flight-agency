package com.example.flight_agency.domine.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger {
    @Id  @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String fullName;


    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "passengerProfile_id")
    private PassengerProfile passengerProfile;

}

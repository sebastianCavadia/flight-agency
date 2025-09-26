package com.example.flight_agency.domine.entities;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat_inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatInventory {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cabin cabin;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;
}

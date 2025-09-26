package com.example.flight_agency.domine.entities;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.*;

import lombok.*;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private OffsetDateTime departureTime;

    @Column(nullable = false)
    private OffsetDateTime arrivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_origin_id")
    private Airport airport_origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_destination_id")
    private Airport airport_destination;

    @OneToMany(mappedBy = "flight")
    private Set<SeatInventory>  seats = new HashSet<>();

    @OneToMany(mappedBy = "flight")
    private List<BookingItem> bookingItems;

    @ManyToMany
    @JoinTable(
            name = "flight_tags",
            joinColumns = @JoinColumn(name = "flight_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Set<Tag> tags = new HashSet<>();
    public void addTag(Tag tag) {
        if (tag != null) {
        tags.add(tag);
        tag.getFlights().add(this);
        }
    }

}

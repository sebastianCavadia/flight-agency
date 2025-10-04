package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.AbstractRepositoryTest;
import com.example.flight_agency.domine.entities.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.example.flight_agency.domine.entities.Airline;
import com.example.flight_agency.domine.entities.Airport;
import com.example.flight_agency.domine.entities.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FlightRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private TagRepository tagRepository;

    private Airline createAirline(String name, String code) {
        Airline airline = Airline.builder().name(name).code(code).build();
        return airlineRepository.save(airline);
    }

    private Airport createAirport(String code, String name, String city) {
        Airport airport = Airport.builder().code(code).name(name).city(city).build();
        return airportRepository.save(airport);
    }


    @Test
    @DisplayName("Buscar vuelo por nombre de aerolinea")
    void findByAirlineName() {
        Airline airline = createAirline("LATAM","LA");
        Flight flight = Flight.builder()
                .airline(airline)
                .number("LA100")
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusDays(3))
                .build();
        flightRepository.save(flight);
        Pageable pageable = PageRequest.of(0, 10);

        List<Flight> result = flightRepository.findFlightByAirlineNameIgnoreCase("LATAM");

        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().getAirline().getName()).isEqualTo("LATAM");
    }

    @Test
    @DisplayName("Buscar por origen, destino y rango fecha")
    void findByOriginAndDestinationAndDepartureTimeBetween() {
        Airport origin = createAirport("BOG","EL DORADO","BOGOTA");
        Airport destination = createAirport("SM","SIMON BOLIVAR","SANTA MARTA");

        OffsetDateTime departureTime = OffsetDateTime.now().plusMinutes(1);

        Flight flight = Flight.builder()
                .number("AV201")
                .airport_origin(origin)
                .airport_destination(destination)
                .departureTime(departureTime)
                .arrivalTime(departureTime.plusHours(1))
                .build();
        flightRepository.save(flight);
        Pageable pageable = PageRequest.of(0, 10);

        OffsetDateTime start = OffsetDateTime.now().minusDays(5);
        OffsetDateTime end = OffsetDateTime.now().plusMinutes(5);

        Page<Flight> foundFlights = flightRepository.
                findByOriginAndDestinationAndDepartureTimeBetween("BOG",
                        "SM", start,
                        end, pageable);


        assertThat(foundFlights).isNotEmpty();
        assertThat(foundFlights.getTotalElements()).isEqualTo(1);
        assertThat(foundFlights.getContent().getFirst().getAirport_origin().
                getCode()).isEqualTo("BOG");
        assertThat(foundFlights.getContent().getFirst().getAirport_destination().
                getCode()).isEqualTo("SM");


    }

    @Test
    @DisplayName("Filtrar por salida/destino")
    void findFlightsWithAssociations() {
        Airline airline = createAirline("AVIANCA","AV");
        Airport origin = createAirport("GU","GUAYMARAL","GUAYMARAL");
        Airport destination = createAirport("GA","GUSTAVO ARTUNDUAGA","FLORENCIA");
        Tag tag = tagRepository.save(Tag.builder().name("promo").build());
        Flight flight = Flight.builder()
                .number("AV322")
                .airline(airline)
                .airport_origin(origin)
                .airport_destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusDays(4))
                .tags(Set.of(tag)).build();
        flightRepository.save(flight);

        List<Flight> foundFlightAll = flightRepository.findFlightsWithAssociations("GU",
                "GA",OffsetDateTime.now().minusMinutes(1),OffsetDateTime.now().plusMinutes(1));
        assertThat(foundFlightAll).isNotEmpty();
        Flight fouFlight1 =  foundFlightAll.getFirst();
        assertThat(fouFlight1.getAirline()).isNotNull();
        assertThat(fouFlight1.getAirport_origin()).isNotNull();
        assertThat(fouFlight1.getAirport_destination()).isNotNull();
        assertThat(fouFlight1.getTags()).isNotNull();
    }

    @Test
    @DisplayName("Buscar vuelos que tiene tags")
    void findFlightsWithAllTags() {
        Tag ta1 = tagRepository.save(Tag.builder().name("promo").build());
        Tag ta2 = tagRepository.save(Tag.builder().name("eco").build());
        Tag ta3 = tagRepository.save(Tag.builder().name("red-eye").build());

        Flight flight = Flight.builder()
                .number("AV356")
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusDays(2))
                .tags(Set.of(ta1,ta2,ta3))
                .build();
        flightRepository.save(flight);
        List<String> tags = List.of("promo", "eco","red-eye");
        List<Flight> result = flightRepository.findFlightsWithAllTags(tags,tags.size());
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(flight.getId());

    }
}
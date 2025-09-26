package com.example.flight_agency.domine.repositories;

import com.example.flight_agency.domine.entities.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
List<Flight> findFlightByAirlineNameIgnoreCase(String airlineName);

    @Query("SELECT f FROM Flight f WHERE f.airport_destination.code = :origin " +
            "AND f.airport_destination.code = :destination " +
            "AND f.departureTime BETWEEN :fechaInicio AND :fechaSalida")
    Page<Flight> findByOriginAndDestinationAndDepartureTimeBetween(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("fechaInicio") OffsetDateTime fechaInicio,
            @Param("fechaSalida") OffsetDateTime fechaSalida,
            Pageable pageable);


    @Query("SELECT DISTINCT f FROM Flight f " +
            "LEFT JOIN FETCH f.airline " +
            "LEFT JOIN FETCH f.airport_origin " +
            "LEFT JOIN FETCH f.airport_destination " +
            "LEFT JOIN FETCH f.tags " +
            "WHERE (:origin IS NULL OR f.airport_origin.code = :origin) " +
            "AND (:destination IS NULL OR f.airport_destination.code = :destination) " +
            "AND f.departureTime BETWEEN :fechaInicio AND :fechaSalida")
    List<Flight> findFlightsWithAssociations(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("fechaInicio") OffsetDateTime fechaInicio,
            @Param("fechaSalida") OffsetDateTime fechaSalida);


    @Query(value = "SELECT DISTINCT f.* FROM flights f " +
            "JOIN FLIGHT_TAGS ft ON f.id = ft.flight_id " +
            "JOIN tags t ON ft.tag_id = t.id " +
            "WHERE t.name IN (:tagNames) " +
            "GROUP BY f.id " +
            "HAVING COUNT(DISTINCT t.name) = :requiredCount",
            nativeQuery = true)
    List<Flight> findFlightsWithAllTags(
            @Param("tags") Collection<String> tags,
            @Param("cantTags") long cantTags);
}

package com.cgi_flight_planner.cgi_flight_planner.repository;

import com.cgi_flight_planner.cgi_flight_planner.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    /* Optional aitab NullPointerExeptionit vältida */
    Optional<Flight> findByFlightNo(String flightNo);

    Optional<Flight> findByDestination(String destination);

    // Täpse Query jaoks kasutasin AI abi
    @Query("SELECT f FROM Flight f WHERE " +
            "(:destinationParam IS NULL OR lower(f.destination) = lower(:destinationParam)) AND " +
            "(:dateParam IS NULL OR CAST(f.departureTime AS date) = :dateParam) AND " +
            "(:maxPriceParam IS NULL OR f.price <= :maxPriceParam)")
    List<Flight> findFlightsByFilter(
            @Param("destinationParam") String destination,
            @Param("dateParam")LocalDate date,
            @Param("maxPriceParam")BigDecimal maxPrice
            );
    List<Flight> findByDepartureTimeBetween(LocalTime start, LocalTime end);
}

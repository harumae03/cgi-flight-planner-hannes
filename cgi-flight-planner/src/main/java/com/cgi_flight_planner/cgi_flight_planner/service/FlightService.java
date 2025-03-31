package com.cgi_flight_planner.cgi_flight_planner.service;

import com.cgi_flight_planner.cgi_flight_planner.model.Flight;
import com.cgi_flight_planner.cgi_flight_planner.model.SeatType;
import com.cgi_flight_planner.cgi_flight_planner.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;


    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public List<Flight> findFlights(String destination, LocalDate date, BigDecimal maxPrice) {
        return flightRepository.findFlightsByFilter(destination, date, maxPrice);
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException("Flight not found with id: " + id + " ei leitud."));
    }

}

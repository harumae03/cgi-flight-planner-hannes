package com.cgi_flight_planner.cgi_flight_planner.service;

import com.cgi_flight_planner.cgi_flight_planner.model.Flight;
import com.cgi_flight_planner.cgi_flight_planner.model.Seat;
import com.cgi_flight_planner.cgi_flight_planner.repository.FlightRepository;
import com.cgi_flight_planner.cgi_flight_planner.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository, FlightRepository flightRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
    }

    public List<Seat> getSeatsForFlight(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new ResourceNotFoundException("Lendu ID-ga " + flightId + " ei leitud, ei saa istmeid pärida.");

        }
        return seatRepository.findByFlightIdOrderBySeatRowAscSeatColumnAsc(flightId);
    }
}

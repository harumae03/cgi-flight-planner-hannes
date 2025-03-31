package com.cgi_flight_planner.cgi_flight_planner.controller;

import com.cgi_flight_planner.cgi_flight_planner.model.Flight;
import com.cgi_flight_planner.cgi_flight_planner.model.Preference;
import com.cgi_flight_planner.cgi_flight_planner.model.Seat;
import com.cgi_flight_planner.cgi_flight_planner.service.FlightService;
import com.cgi_flight_planner.cgi_flight_planner.service.SeatRecommendationService;
import com.cgi_flight_planner.cgi_flight_planner.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

// Otsisin googlest ja AI käest küsisin kuidas selliseid asju teha
@RestController
@RequestMapping("/api")
public class FlightController {

    private final FlightService flightService;
    private final SeatService seatService;
    private final SeatRecommendationService seatRecommendationService;

    @Autowired
    public FlightController(FlightService flightService, SeatService seatService,
                            SeatRecommendationService seatRecommendationService) {
        this.flightService = flightService;
        this.seatService = seatService;
        this.seatRecommendationService = seatRecommendationService;
    }

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> findFlights (
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) BigDecimal maxPrice

    ){
        List<Flight> flights= flightService.findFlights(destination, date, maxPrice);
        return ResponseEntity.ok(flights);

    }


    @GetMapping("/flights/{flightId}/seats")
    public ResponseEntity<List<Seat>> getSeatMap (@PathVariable Long flightId){
        List<Seat> seats = seatService.getSeatsForFlight(flightId);
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/flights/{flightId}/recommend-seats")
    public ResponseEntity<List<Seat>> recommendSeats (
            @PathVariable Long flightId,
            @RequestParam int count, // reisijate arv
            @RequestParam(required = false)Set<Preference> preferences){
        Set<Preference> effectivePreferences = (preferences != null) ? preferences : EnumSet.noneOf(Preference.class);

        List<Seat> recommendedSeats = seatRecommendationService.recommendSeats(flightId, count, effectivePreferences);
        return  ResponseEntity.ok(recommendedSeats);
    }
    @GetMapping("/flights/{flightId}")
    public ResponseEntity<Flight> getFlightDetails(@PathVariable Long flightId) {
        Flight flight = flightService.getFlightById(flightId);
        return ResponseEntity.ok(flight);
    }
}

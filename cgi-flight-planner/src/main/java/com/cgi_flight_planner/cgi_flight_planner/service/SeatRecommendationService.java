package com.cgi_flight_planner.cgi_flight_planner.service;

import com.cgi_flight_planner.cgi_flight_planner.model.Preference;
import com.cgi_flight_planner.cgi_flight_planner.model.Seat;
import com.cgi_flight_planner.cgi_flight_planner.model.SeatType;
import com.cgi_flight_planner.cgi_flight_planner.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatRecommendationService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatRecommendationService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> recommendSeats(Long flightId, int numberOfSeats, Set<Preference> preferences) {
        List<Seat> availableSeats = seatRepository.findByFlightIdAndIsOccupiedFalseOrderBySeatRowAscSeatColumnAsc(flightId);
        List<Seat> filteredSeats = availableSeats;

        if (preferences != null && !preferences.isEmpty()) {
            if (preferences.contains(Preference.WINDOW)){
                filteredSeats = availableSeats.stream().filter(seat -> seat.getType() == SeatType.WINDOW).collect(Collectors.toList());
            } else if (preferences.contains(Preference.AISLE)) {
                filteredSeats = availableSeats.stream().filter(seat -> seat.getType() == SeatType.AISLE).collect(Collectors.toList());
            } else if (preferences.contains(Preference.EXTRA_LEGROOM)) {
                filteredSeats = availableSeats.stream().filter(Seat::isHasExtraLegroom).collect(Collectors.toList());
            }
        }
        return filteredSeats.stream().limit(numberOfSeats).collect(Collectors.toList());
    }
}

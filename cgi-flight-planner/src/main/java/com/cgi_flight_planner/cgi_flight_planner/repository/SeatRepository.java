package com.cgi_flight_planner.cgi_flight_planner.repository;

import com.cgi_flight_planner.cgi_flight_planner.model.Seat;
import com.cgi_flight_planner.cgi_flight_planner.model.SeatClass;
import com.cgi_flight_planner.cgi_flight_planner.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    //Kasutasin AI-d et leida täpsed nimed

    //leiab KÕIK istekohad
    List<Seat> findByFlightIdOrderBySeatRowAscSeatColumnAsc(Long flightId);

    //leiab vabad istekohad
    List<Seat> findByFlightIdAndIsOccupiedFalseOrderBySeatRowAscSeatColumnAsc(Long flightId);

    //leiab istekohad tüübi kaupa
    List<Seat> findByFlightIdAndIsOccupiedFalseAndTypeInOrderBySeatRowAscSeatColumnAsc(Long flightId, Set<SeatType> types);

    //Leiab vabad isetkohad varuväljapääsu reas
    List<Seat> findByFlightIdAndIsOccupiedFalseAndIsExitRowTrueOrderBySeatRowAscSeatColumnAsc(Long flightId);

    //leiab vabad istekohad, millel on suurem jalaruum
    List<Seat> findByFlightIdAndIsOccupiedFalseAndHasExtraLegroomTrueOrderBySeatRowAscSeatColumnAsc(Long flightId);

    //leiab istekohad klassi kaupa
    List<Seat> findByFlightIdAndIsOccupiedFalseAndSeatClassOrderBySeatRowAscSeatColumnAsc(Long flightId, SeatClass seatClass);


}

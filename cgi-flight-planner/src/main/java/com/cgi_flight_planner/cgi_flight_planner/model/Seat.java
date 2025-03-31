package com.cgi_flight_planner.cgi_flight_planner.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//laeb vajadusel
    @JoinColumn(name = "flight_id", nullable = false)

    private Flight flight;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private SeatType seatRow;

    @Column(nullable = false)
    private char seatColumn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatClass seatClass;

    @Column(nullable = false)
    private boolean isExitRow = false;

    @Column(nullable = false)
    private boolean hasExtraLegroom = false;

    @Column(nullable = false)
    private boolean isOccupied = false;
}

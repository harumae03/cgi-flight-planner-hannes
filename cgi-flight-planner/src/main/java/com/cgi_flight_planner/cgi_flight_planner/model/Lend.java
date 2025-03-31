package com.cgi_flight_planner.cgi_flight_planner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

@Entity
@Table(name = "lennud")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)//sest lennunumber on väga tähtis
    private String flightNo;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false, precision =  19, scale = 2)
    private BigDecimal price;

    private String airline;

}

DROP TABLE IF EXISTS seats CASCADE;
DROP TABLE IF EXISTS flights CASCADE;
DROP TABLE IF EXISTS aircraft_layouts CASCADE;

CREATE TABLE aircraft_layouts (
                                  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                  name VARCHAR(255) NOT NULL UNIQUE,
                                  total_rows INT NOT NULL,
                                  seat_configuration VARCHAR(255) NOT NULL
);

CREATE TABLE flights (
                         id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                         flight_number VARCHAR(255) NOT NULL UNIQUE,
                         origin VARCHAR(255) NOT NULL,
                         destination VARCHAR(255) NOT NULL,
                         departure_time TIMESTAMP NOT NULL, -- Õige tüüp LocalDateTime jaoks
                         arrival_time TIMESTAMP NOT NULL,   -- Õige tüüp LocalDateTime jaoks
                         price DECIMAL(10, 2) NOT NULL,
                         airline VARCHAR(255),
                         aircraft_layout_id BIGINT NOT NULL,
                         FOREIGN KEY (aircraft_layout_id) REFERENCES aircraft_layouts(id)
);

CREATE TABLE seats (
                       id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       flight_id BIGINT NOT NULL,
                       seat_number VARCHAR(255) NOT NULL,
                       seat_row INT NOT NULL,
                       seat_column CHAR(1) NOT NULL,
                       type VARCHAR(50) NOT NULL,
                       seat_class VARCHAR(50) NOT NULL,
                       is_exit_row BOOLEAN NOT NULL DEFAULT FALSE,
                       has_extra_legroom BOOLEAN NOT NULL DEFAULT FALSE,
                       is_occupied BOOLEAN NOT NULL DEFAULT FALSE,
                       FOREIGN KEY (flight_id) REFERENCES flights(id)
);
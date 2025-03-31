-- Kasutasin AI-d et täpsed lennuplaani queryd
-- lisada kui andmebaasi käivitan, et oleks mida vaadata ja aega säästa.

-- Eeldame, et ID genereeritakse automaatselt (järjekorras 1, 2, ...)
INSERT INTO aircraft_layouts (name, total_rows, seat_configuration) VALUES
                                                                        ('Boeing 737-800 (189 Seats)', 32, 'ABC-DEF'), -- ID = 1
                                                                        ('ATR 72-600 (70 Seats)', 18, 'AC-DF');      -- ID = 2

-- Lennud (Flights)

INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, price, airline, aircraft_layout_id) VALUES
                                                                                                                               ('SK789', 'TLL', 'ARN', '2024-10-20 10:00:00', '2024-10-20 10:50:00', 185.50, 'SAS', 1),      -- ID = 1, kasutab Boeing 737
                                                                                                                               ('BT321', 'TLL', 'RIX', '2024-10-21 12:30:00', '2024-10-21 13:20:00', 120.00, 'AirBaltic', 2), -- ID = 2, kasutab ATR 72
                                                                                                                               ('AY102', 'TLL', 'HEL', '2024-10-20 09:15:00', '2024-10-20 09:45:00', 145.75, 'Finnair', 2); -- ID = 3, kasutab ATR 72

-- Istekohad (Seats)

-- Istekohad lennule ID=1 (SK789, Boeing 737)
-- Rida 1 (Eeldame, et see on Exit Row ja Economy Plus klass)
INSERT INTO seats (flight_id, seat_number, seat_row, seat_column, type, seat_class, is_exit_row, has_extra_legroom, is_occupied) VALUES
                                                                                                                                     (1, '1A', 1, 'A', 'WINDOW', 'ECONOMY_PLUS', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '1B', 1, 'B', 'MIDDLE', 'ECONOMY_PLUS', TRUE, TRUE, TRUE), -- Hõivatud
                                                                                                                                     (1, '1C', 1, 'C', 'AISLE', 'ECONOMY_PLUS', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '1D', 1, 'D', 'AISLE', 'ECONOMY_PLUS', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '1E', 1, 'E', 'MIDDLE', 'ECONOMY_PLUS', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '1F', 1, 'F', 'WINDOW', 'ECONOMY_PLUS', TRUE, TRUE, TRUE); -- Hõivatud

-- Rida 2 (Tavaline turistiklass)
INSERT INTO seats (flight_id, seat_number, seat_row, seat_column, type, seat_class, is_exit_row, has_extra_legroom, is_occupied) VALUES
                                                                                                                                     (1, '2A', 2, 'A', 'WINDOW', 'ECONOMY', FALSE, FALSE, TRUE), -- Hõivatud
                                                                                                                                     (1, '2B', 2, 'B', 'MIDDLE', 'ECONOMY', FALSE, FALSE, FALSE),
                                                                                                                                     (1, '2C', 2, 'C', 'AISLE', 'ECONOMY', FALSE, FALSE, FALSE),
                                                                                                                                     (1, '2D', 2, 'D', 'AISLE', 'ECONOMY', FALSE, FALSE, TRUE), -- Hõivatud
                                                                                                                                     (1, '2E', 2, 'E', 'MIDDLE', 'ECONOMY', FALSE, FALSE, FALSE),
                                                                                                                                     (1, '2F', 2, 'F', 'WINDOW', 'ECONOMY', FALSE, FALSE, FALSE);

-- Rida 10 (Eeldame, et Exit Row ilma lisaklassita)
INSERT INTO seats (flight_id, seat_number, seat_row, seat_column, type, seat_class, is_exit_row, has_extra_legroom, is_occupied) VALUES
                                                                                                                                     (1, '10A', 10, 'A', 'WINDOW', 'ECONOMY', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '10B', 10, 'B', 'MIDDLE', 'ECONOMY', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '10C', 10, 'C', 'AISLE', 'ECONOMY', TRUE, TRUE, TRUE), -- Hõivatud
                                                                                                                                     (1, '10D', 10, 'D', 'AISLE', 'ECONOMY', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '10E', 10, 'E', 'MIDDLE', 'ECONOMY', TRUE, TRUE, FALSE),
                                                                                                                                     (1, '10F', 10, 'F', 'WINDOW', 'ECONOMY', TRUE, TRUE, FALSE);


-- Istekohad lennule ID=2 (BT321, ATR 72) - Paigutus AC-DF
-- Rida 1 (Eeldame äriklassi vms, rohkem ruumi)
INSERT INTO seats (flight_id, seat_number, seat_row, seat_column, type, seat_class, is_exit_row, has_extra_legroom, is_occupied) VALUES
                                                                                                                                     (2, '1A', 1, 'A', 'WINDOW', 'BUSINESS', FALSE, TRUE, FALSE),
                                                                                                                                     (2, '1C', 1, 'C', 'AISLE', 'BUSINESS', FALSE, TRUE, TRUE), -- Hõivatud
                                                                                                                                     (2, '1D', 1, 'D', 'AISLE', 'BUSINESS', FALSE, TRUE, FALSE),
                                                                                                                                     (2, '1F', 1, 'F', 'WINDOW', 'BUSINESS', FALSE, TRUE, FALSE);

-- Rida 2 (Turistiklass)
INSERT INTO seats (flight_id, seat_number, seat_row, seat_column, type, seat_class, is_exit_row, has_extra_legroom, is_occupied) VALUES
                                                                                                                                     (2, '2A', 2, 'A', 'WINDOW', 'ECONOMY', FALSE, FALSE, FALSE),
                                                                                                                                     (2, '2C', 2, 'C', 'AISLE', 'ECONOMY', FALSE, FALSE, FALSE),
                                                                                                                                     (2, '2D', 2, 'D', 'AISLE', 'ECONOMY', FALSE, FALSE, TRUE), -- Hõivatud
                                                                                                                                     (2, '2F', 2, 'F', 'WINDOW', 'ECONOMY', FALSE, FALSE, FALSE);



-- Istekohad lennule ID=3 (AY102, ATR 72) - Paigutus AC-DF
INSERT INTO seats (flight_id, seat_number, seat_row, seat_column, type, seat_class, is_exit_row, has_extra_legroom, is_occupied) VALUES
                                                                                                                                     (3, '1A', 1, 'A', 'WINDOW', 'ECONOMY', FALSE, FALSE, TRUE), -- Hõivatud
                                                                                                                                     (3, '1C', 1, 'C', 'AISLE', 'ECONOMY', FALSE, FALSE, FALSE),
                                                                                                                                     (3, '1D', 1, 'D', 'AISLE', 'ECONOMY', FALSE, FALSE, FALSE),
                                                                                                                                     (3, '1F', 1, 'F', 'WINDOW', 'ECONOMY', FALSE, FALSE, TRUE); -- Hõivatud

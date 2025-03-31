document.addEventListener('DOMContentLoaded', () => {
    // DOM elemendid
    const destinationInput = document.getElementById('destination');
    const dateInput = document.getElementById('date');
    const maxPriceInput = document.getElementById('maxPrice');
    const searchButton = document.getElementById('search-button');
    const flightsListDiv = document.getElementById('flights-list');
    const flightsSection = document.getElementById('flights-section');
    const seatsSection = document.getElementById('seats-section');
    const seatMapDiv = document.getElementById('seat-map');
    const selectedFlightSpan = document.getElementById('selected-flight-number');
    const seatCountInput = document.getElementById('seat-count');
    const recommendButton = document.getElementById('recommend-button');
    const backButton = document.getElementById('back-to-flights-button');

    let currentFlightId = null; // Hoiame valitud lennu ID-d

    // --- Funktsioonid ---

    // Lendude laadimine ja kuvamine
    async function loadFlights() {
        flightsListDiv.innerHTML = 'Laen lende...';
        let url = '/api/flights?';
        const params = new URLSearchParams();
        if (destinationInput.value) params.append('destination', destinationInput.value);
        if (dateInput.value) params.append('date', dateInput.value);
        if (maxPriceInput.value) params.append('maxPrice', maxPriceInput.value);
        // Lisa siia teised filtrid (airline, maxDuration), kui need controlleris on

        url += params.toString();

        try {
            const response = await fetch(url);
            if (!response.ok) throw new Error(`HTTP viga! staatus: ${response.status}`);
            const flights = await response.json();
            displayFlights(flights);
        } catch (error) {
            flightsListDiv.innerHTML = `Viga lendude laadimisel: ${error.message}`;
            console.error("Fetch error:", error);
        }
    }

    // Lendude kuvamine HTMLis
    function displayFlights(flights) {
        flightsListDiv.innerHTML = ''; // Tühjenda eelmine sisu
        if (flights.length === 0) {
            flightsListDiv.innerHTML = 'Vastavaid lende ei leitud.';
            return;
        }
        flights.forEach(flight => {
            const flightDiv = document.createElement('div');
            // Vorminda kuupäev/kellaaeg loetavamaks (vajab kohandamist)
            const departure = new Date(flight.departureTime).toLocaleString();
            const arrival = new Date(flight.arrivalTime).toLocaleString();

            flightDiv.innerHTML = `
                <strong>Lend ${flight.flightNumber}</strong> (${flight.airline})<br>
                ${flight.origin} -> ${flight.destination}<br>
                Väljumine: ${departure}<br>
                Saabumine: ${arrival}<br>
                Hind: ${flight.price} EUR<br>
                <button class="select-seat-button" data-flight-id="${flight.id}" data-flight-number="${flight.flightNumber}">Vali istekoht</button>
            `;
            flightsListDiv.appendChild(flightDiv);
        });

        // Lisa nupule sündmuskuulaja
        document.querySelectorAll('.select-seat-button').forEach(button => {
            button.addEventListener('click', handleSelectSeatClick);
        });
    }

    // Istekoha valimise nupu käsitlemine
    async function handleSelectSeatClick(event) {
        currentFlightId = event.target.dataset.flightId;
        const flightNumber = event.target.dataset.flightNumber;
        selectedFlightSpan.textContent = flightNumber; // Näita lennu numbrit

        flightsSection.style.display = 'none';
        seatsSection.style.display = 'block';
        seatMapDiv.innerHTML = 'Laen istmeplaani...';

        await loadAndDisplaySeatMap(currentFlightId);
    }

    // Istmeplaani laadimine ja kuvamine
    async function loadAndDisplaySeatMap(flightId) {
        try {
            const response = await fetch(`/api/flights/${flightId}/seats`);
            if (!response.ok) throw new Error(`HTTP viga! staatus: ${response.status}`);
            const seats = await response.json();
            renderSeatMap(seats);
        } catch (error) {
            seatMapDiv.innerHTML = `Viga istmeplaani laadimisel: ${error.message}`;
            console.error("Fetch error:", error);
        }
    }

    // Istmeplaani renderdamine HTMLis (vajab kohandamist vastavalt lennukiplaanile)
    function renderSeatMap(seats) {
        seatMapDiv.innerHTML = '';
        if (seats.length === 0) return;

        // Eeldame, et 'seats' on sorteeritud rea ja veeru järgi back-endist
        let currentRow = -1;
        const layoutConfig = "ABC-DEF"; // VÕTA SEE REAALSELT flight.aircraftLayout.seatConfiguration põhjal!
        const columns = layoutConfig.replace('-', '').split(''); // ['A', 'B', 'C', 'D', 'E', 'F']

        seats.forEach(seat => {
            // Kui uus rida, lisa rea number (lihtsustatud)
            if (seat.seatRow !== currentRow) {
                if (currentRow !== -1) seatMapDiv.appendChild(document.createElement('br')); // Reavahetus
                const rowLabel = document.createElement('div');
                rowLabel.textContent = `Rida ${seat.seatRow}`;
                rowLabel.style.gridColumn = `span ${layoutConfig.length}`; // Võtab terve rea laiuse
                rowLabel.style.textAlign = 'center';
                rowLabel.style.fontWeight = 'bold';
                // seatMapDiv.appendChild(rowLabel); // Võib segada gridi, lisa ettevaatlikult
                currentRow = seat.seatRow;
                // Lisa tühjad kohad rea algusesse, kui vaja
                for(let i = 0; i < columns.indexOf(seat.seatColumn.toString()); i++){
                    if(layoutConfig[i] === '-') {
                        const gapDiv = document.createElement('div');
                        gapDiv.classList.add('seat-aisle-gap');
                        seatMapDiv.appendChild(gapDiv);
                    } else {
                        const emptyDiv = document.createElement('div'); // Tühi koht
                        seatMapDiv.appendChild(emptyDiv);
                    }
                }
            }

            // Lisa vahekäik, kui vaja
            const currentColumnIndex = columns.indexOf(seat.seatColumn.toString());
            if (currentColumnIndex > 0 && layoutConfig[currentColumnIndex] !== '-' && layoutConfig[currentColumnIndex-1] === '-') {
                const gapDiv = document.createElement('div');
                gapDiv.classList.add('seat-aisle-gap');
                seatMapDiv.appendChild(gapDiv);
            }

            const seatDiv = document.createElement('div');
            seatDiv.classList.add('seat');
            seatDiv.dataset.seatId = seat.id;
            seatDiv.textContent = seat.seatNumber; // Või ainult veerg: seat.seatColumn

            // Lisa klassid staatuse ja omaduste järgi
            seatDiv.classList.add(seat.occupied ? 'occupied' : 'available');
            if (!seat.occupied) {
                if (seat.type === 'WINDOW') seatDiv.classList.add('window');
                if (seat.type === 'AISLE') seatDiv.classList.add('aisle');
                if (seat.hasExtraLegroom) seatDiv.classList.add('extra-legroom');
                if (seat.isExitRow) seatDiv.classList.add('exit-row');
            } else {
                seatDiv.title = "Hõivatud";
            }

            seatMapDiv.appendChild(seatDiv);
        });
    }

    // Istmesoovituste küsimine ja esiletõstmine
    async function handleRecommendSeats() {
        if (!currentFlightId) return;
        const count = seatCountInput.value;
        const preferences = [];
        document.querySelectorAll('#preferences-section input[type="checkbox"]:checked').forEach(cb => {
            preferences.push(cb.value);
        });

        const params = new URLSearchParams();
        params.append('count', count);
        if (preferences.length > 0) {
            params.append('preferences', preferences.join(','));
        }

        try {
            const response = await fetch(`/api/flights/${currentFlightId}/recommend-seats?${params.toString()}`);
            if (!response.ok) throw new Error(`HTTP viga! staatus: ${response.status}`);
            const recommendedSeats = await response.json();

            // Eemalda eelmine esiletõstmine
            document.querySelectorAll('.seat.recommended').forEach(el => el.classList.remove('recommended'));

            // Tõsta uued esile
            recommendedSeats.forEach(recSeat => {
                const seatDiv = seatMapDiv.querySelector(`.seat[data-seat-id="${recSeat.id}"]`);
                if (seatDiv) {
                    seatDiv.classList.add('recommended');
                }
            });

        } catch(error) {
            console.error("Soovituste viga:", error);
            alert(`Viga soovituste saamisel: ${error.message}`);
        }
    }

    // Tagasi lendude juurde nupp
    function handleBackToFlights() {
        seatsSection.style.display = 'none';
        flightsSection.style.display = 'block';
        currentFlightId = null; // Nulli valitud lend
    }


    // --- Sündmuskuulajad ---
    searchButton.addEventListener('click', loadFlights);
    recommendButton.addEventListener('click', handleRecommendSeats);
    backButton.addEventListener('click', handleBackToFlights);


    // --- Esmane laadimine ---
    loadFlights();

});
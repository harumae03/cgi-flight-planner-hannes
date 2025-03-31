See projekt on loodud CGI suvepraktika 2025 kandideerimise ülesandena. Rakenduse eesmärk on pakkuda kasutajale võimalust otsida lende erinevate filtrite alusel ja saada soovitusi istekoha valikuks lennuki plaanil.

**Märkus:** Ajalise piirangu tõttu on mõned funktsionaalsused lihtsustatud (nt istekoha soovitamise loogika, front-end'i detailid). Täpsemalt on kirjeldatud allpool.

## Tehnoloogiad

*   **Back-end:**
    *   Java 21 (Corretto JDK)
    *   Spring Boot 3.4.4
    *   Spring Data JPA
    *   Hibernate
    *   Maven (projektihaldus ja sõltuvused)
    *   H2 Database (mälusisene SQL andmebaas arenduseks)
    *   Lombok (vähendab boilerplate koodi)
*   **Front-end:**
    *   HTML5
    *   CSS3
    *   Vanilla JavaScript (ilma raamistiketa)
*   **Versioonikontroll:** Git
*   **Koodihoidla:** GitHub (või Bitbucket)

## Funktsionaalsus

### Põhifunktsionaalsus (Implementeeritud)

1.  **Lendude Otsing ja Filtreerimine:**
    *   Kasutaja näeb algselt kõiki saadaolevaid lende.
    *   Lende saab filtreerida järgmiste kriteeriumite alusel (API endpoint `/api/flights`):
        *   Sihtkoht (`destination`)
        *   Väljumiskuupäev (`date`)
        *   Maksimaalne hind (`maxPrice`)
        *   Lennufirma (`airline`)
        *   Maksimaalne lennu kestvus (`maxDuration`, nt "PT1H30M") - *Märkus: kestvuse filter rakendatakse back-end service'is Java koodis, mitte otse andmebaasi päringus.*
2.  **Istmeplaani Kuvamine:** EI TÖÖTA!
    *   Pärast lennu valimist kuvatakse kasutajale selle lennu istmeplaan (API endpoint `/api/flights/{flightId}/seats`).
    *   Plaanil on näha:
        *   Istekoha number
        *   Hõivatud/vaba staatus
        *   Visuaalsed indikatsioonid (lihtsustatud) istekoha tüübi (aken/vahekäik), lisajalaruumi ja väljapääsurea kohta.
3.  **Istekoha Soovitamine (Lihtsustatud):** EI TÖÖTA
    *   Kasutaja saab valida soovitud kohtade arvu ja eelistused (aken, vahekäik, lisajalaruum, väljapääsu rida, kõrvuti).
    *   Rakendus küsib back-endist soovitusi (API endpoint `/api/flights/{flightId}/recommend-seats`).
    *   Soovitatud kohad tõstetakse istmeplaanil esile.
    *   **LIHTSUSTUS:** Back-endi `SeatRecommendationService` implementeerib hetkel **väga lihtsa** soovituste loogika. See filtreerib kohti peamiselt ühe eelistuse alusel (nt `WINDOW` või `AISLE` või `EXTRA_LEGROOM`) ja tagastab esimesed leitud vabad kohad. Keerulisemad kombinatsioonid ja `TOGETHER` (kõrvuti) ning `EXIT_ROW` prioriseerimine **ei pruugi täielikult korrektselt töötada** ja vajaksid edasist arendust (vt "Võimalikud Parendused").
4.  **Andmete Initsialiseerimine:**
    *   Rakenduse käivitamisel täidetakse H2 mälusisene andmebaas näidisandmetega (`aircraft_layouts`, `flights`, `seats`) failist `src/main/resources/data.sql`.
    *   Skeem luuakse kas Hibernate `ddl-auto=create-drop` seade või `src/main/resources/schema.sql` faili abil (kontrolli `application.properties`!).


## Käivitamine

1.  **Eeltingimused:**
    *   Java Development Kit (JDK) versioon 21 (või uuem LTS) peab olema installitud ja `JAVA_HOME` seadistatud.
    *   Apache Maven peab olema installitud (või kasutad IDE sisseehitatud Mavenit).
    *   Git peab olema installitud.
2.  **Koodi Kloonimine:**
    ```bash
    git clone <sinu_repo_url>
    cd <projekti_kataloog>
    ```
3.  **Rakenduse Käivitamine (Maveniga):**
    *   Ava terminal/käsurida projekti juurkataloogis.
    *   Käivita käsk:
        ```bash
        mvn spring-boot:run
        ```
    *   Rakendus peaks käivituma ja olema kättesaadav aadressil `http://localhost:8080`.
4.  **Rakenduse Kasutamine:**
    *   Ava veebibrauseris `http://localhost:8080`.
    *   Kasuta filtreid ja nuppe lendude otsimiseks ja istmete vaatamiseks/soovitamiseks.
5.  **Andmebaasi Sirvimine (H2 Console):**
    *   Ava veebibrauseris `http://localhost:8080/h2-console`.
    *   Veendu, et **JDBC URL** on `jdbc:h2:mem:flightdb`.
    *   Kasutajanimi: `sa`
    *   Parool: `password` (või tühi, kui jätsid selle `application.properties` failis tühjaks).
    *   Kliki "Connect". Nüüd saad vaadata tabeleid (`FLIGHTS`, `SEATS` jne) ja nende sisu.

## Tehtud Otsused ja Lihtsustused

*   **Andmebaas:** Kasutatud H2 mälusisest andmebaasi lihtsuse ja kiire seadistamise huvides. Produktsioonis kasutataks tõenäoliselt PostgreSQL vms.
*   **Front-end:** Kasutatud lihtsat Vanilla JavaScripti, et vältida keerukate raamistike seadistamist. Kasutajaliides on funktsionaalne, kuid visuaalselt lihtne. Istmeplaani renderdamine (`renderSeatMap` funktsioon `script.js`-is) vajab kohandamist vastavalt konkreetsele lennukiplaanile (`seatConfiguration`), hetkel on see näidislahendus.
*   **Istekoha Soovitamine:** Nagu mainitud, on `SeatRecommendationService` lihtsustatud. Arendamist vajaksid:
    *   Korrektne `TOGETHER` (kõrvuti) kohtade leidmine, mis arvestab vahekäike (kasutades `AircraftLayout.seatConfiguration`).
    *   Mitme eelistuse (nt `WINDOW` + `EXIT_ROW`) korrektne kombineerimine ja prioriseerimine.
    *   "Lähedal väljapääsule" täpsem defineerimine (praegu võib `EXIT_ROW` eelistus toimida lihtsa lipuna).

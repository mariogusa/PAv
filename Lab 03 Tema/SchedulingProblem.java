import java.time.LocalTime;
import java.util.*;

public class SchedulingProblem {
    private Airport airport;
    private Set<Flight> flights;

    public SchedulingProblem(Airport airport, Set<Flight> flights) {
        this.airport = airport;
        this.flights = flights;
    }

    /**
     * A simple algorithm to assign each flight to a runway:
     * 1) Sort by landing start time
     * 2) Keep track of the earliest time each runway is available
     * 3) For each flight, pick the first runway that is free before (or at) the flight's start time
     * 4) If none is free, we cannot schedule that flight
     */
    
    public Map<Flight, String> solve() {
        List<Flight> sortedFlights = new ArrayList<>(flights);
        sortedFlights.sort(Comparator.comparing(Flight::getLandingStart));

        Map<String, LocalTime> runwayAvailability = new HashMap<>();
        for (String runway : airport.getRunways()) {
            runwayAvailability.put(runway, LocalTime.MIN);
        }

        Map<Flight, String> assignment = new HashMap<>();

        for (Flight flight : sortedFlights) {
            String chosenRunway = null;

            for (String runway : airport.getRunways()) {
                LocalTime availableFrom = runwayAvailability.get(runway);
                if (!availableFrom.isAfter(flight.getLandingStart())) {
                    chosenRunway = runway;
                    runwayAvailability.put(runway, flight.getLandingEnd());
                    break;
                }
            }

            if (chosenRunway != null) {
                assignment.put(flight, chosenRunway);
            } else {
                System.out.println("Cannot schedule flight " + flight.getFlightNumber() +
                        " in the requested interval " + flight.getLandingStart() + " - " + flight.getLandingEnd());
            }
        }

        return assignment;
    }
}
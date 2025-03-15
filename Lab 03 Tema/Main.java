import java.time.LocalTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Airport airport = new Airport(
            "International Airport",
            Arrays.asList("Runway-A", "Runway-B", "Runway-C")
        );

        Set<Flight> flights = new HashSet<>();
        flights.add(new Flight("A1", LocalTime.of(10, 5), LocalTime.of(10, 20)));
        flights.add(new Flight("B2", LocalTime.of(10, 15), LocalTime.of(10, 30)));
        flights.add(new Flight("C3", LocalTime.of(10, 25), LocalTime.of(10, 45)));
        flights.add(new Flight("D4", LocalTime.of(10, 5), LocalTime.of(10, 30)));
        flights.add(new Flight("E5", LocalTime.of(10, 0), LocalTime.of(10, 30)));

        SchedulingProblem problem = new SchedulingProblem(airport, flights);

        Map<Flight, String> assignment = problem.solve();

        System.out.println("\nScheduling results:");
        for (Map.Entry<Flight, String> entry : assignment.entrySet()) {
            Flight flight = entry.getKey();
            String runway = entry.getValue();
            System.out.println("Flight " + flight.getFlightNumber() +
                    " (" + flight.getLandingStart() + " - " + flight.getLandingEnd() + ")" +
                    " -> " + runway);
        }
    }
}
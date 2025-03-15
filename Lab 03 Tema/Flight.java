import java.time.LocalTime;

public class Flight {
    private String flightNumber;
    private LocalTime landingStart;
    private LocalTime landingEnd;

    public Flight(String flightNumber, LocalTime landingStart, LocalTime landingEnd) {
        this.flightNumber = flightNumber;
        this.landingStart = landingStart;
        this.landingEnd = landingEnd;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public LocalTime getLandingStart() {
        return landingStart;
    }

    public LocalTime getLandingEnd() {
        return landingEnd;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", landingStart=" + landingStart +
                ", landingEnd=" + landingEnd +
                '}';
    }
}
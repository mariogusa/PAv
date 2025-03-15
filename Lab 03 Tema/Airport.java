import java.util.List;

public class Airport {
    private String name;
    private List<String> runways;

    public Airport(String name, List<String> runways) {
        this.name = name;
        this.runways = runways;
    }

    public String getName() {
        return name;
    }

    public List<String> getRunways() {
        return runways;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", runways=" + runways +
                '}';
    }
}
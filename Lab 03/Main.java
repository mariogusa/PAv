import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Airliner a1 = new Airliner("Boeing 737", 200);
        Airliner a2 = new Airliner("Airbus A320", 150);
        Freighter b1 = new Freighter("Cargo King", 20000);
        Drone b2 = new Drone("Delivery Drone", 100);

        Aircraft[] allAircraft = { a1, a2, b1, b2 };

        List<Aircraft> cargoCapableAircraft = new ArrayList<>();
        for (Aircraft ac : allAircraft) {
            if (ac instanceof CargoCapable) {
                cargoCapableAircraft.add(ac);
            }
        }

        System.out.println("All cargo-capable aircraft:");
        for (Aircraft ac : cargoCapableAircraft) {
            System.out.println(" - " + ac.getName());
        }

        System.out.println("\nTransporting cargo with each cargo-capable aircraft:");
        for (Aircraft ac : cargoCapableAircraft) {
            ((CargoCapable)ac).transportCargo(500); 
        }
    }
}
public class Drone extends Aircraft implements CargoCapable {

    private double cargoCapacity;

    public Drone(String name, double cargoCapacity) {
        super(name);
        this.cargoCapacity = cargoCapacity;
    }

    @Override
    public void transportCargo(double weight) {
        if (weight <= cargoCapacity) {
            System.out.println(getName() + " drone is transporting " 
                               + weight + " kg of cargo.");
        } else {
            System.out.println(getName() + " drone cannot transport " 
                               + weight + " kg (exceeds capacity).");
        }
    }
}
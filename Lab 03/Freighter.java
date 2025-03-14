public class Freighter extends Aircraft implements CargoCapable {

    private double cargoCapacity;

    public Freighter(String name, double cargoCapacity) {
        super(name);
        this.cargoCapacity = cargoCapacity;
    }

    @Override
    public void transportCargo(double weight) {
        if (weight <= cargoCapacity) {
            System.out.println(getName() + " is transporting " 
                               + weight + " kg of cargo.");
        } else {
            System.out.println(getName() + " cannot transport " 
                               + weight + " kg (exceeds capacity).");
        }
    }
}
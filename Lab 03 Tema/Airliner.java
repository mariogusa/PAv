public class Airliner extends Aircraft implements PassengerCapable {

    private int passengerCapacity;

    public Airliner(String name, int passengerCapacity) {
        super(name);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public void transportPassengers(int passengerCount) {
        if (passengerCount <= passengerCapacity) {
            System.out.println(getName() + " is transporting " 
                               + passengerCount + " passengers.");
        } else {
            System.out.println(getName() + " cannot transport " 
                               + passengerCount + " passengers (exceeds capacity).");
        }
    }
}
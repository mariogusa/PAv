public abstract class Aircraft implements Comparable<Aircraft> {
    private String name;

    public Aircraft(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Aircraft other) {
        return this.name.compareTo(other.name);
    }
}
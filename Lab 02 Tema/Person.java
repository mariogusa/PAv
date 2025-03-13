public class Person {
    private String name;
    private String dateOfBirth;

    private static final int MAX_PERSONS = 100;
    private static final Person[] allPersons = new Person[MAX_PERSONS];
    private static int personCount = 0;

    public Person(String name, String dateOfBirth) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        addPersonToList(this);
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    private static void addPersonToList(Person person) {
        if (personCount < MAX_PERSONS) {
            allPersons[personCount++] = person;
        } else {
            System.out.println("Maximum number of Person objects reached. Cannot add more.");
        }
    }

    public static Person[] getAllPersons() {
        Person[] result = new Person[personCount];
        for (int i = 0; i < personCount; i++) {
            result[i] = allPersons[i];
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Person)) return false;
        Person other = (Person) obj;
        return this.name.equals(other.name) && this.dateOfBirth.equals(other.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return (name + dateOfBirth).hashCode();
    }
}
public class Main {
    public static void main(String[] args) {
        Teacher teacher = new Teacher("Prof. Andrei", "1982-08-06", 3);
        
        teacher.addProject(new Project("Math Research", "Algebra"));
        teacher.addProject(new Project("Physics Experiment", "Projectiles"));
        
        Student student1 = new Student("Stefan", "2003-04-12", "S1001");
        Student student2 = new Student("Razvan",   "2002-01-07", "S1002");

        ProjectAggregator.assignProjectToStudent(teacher, student1, "Math Research");

        ProjectAggregator.assignProjectToStudent(teacher, student2, "Chemistry");

        Student student3 = new Student("Stefan", "2003-04-12", "S1003");
        System.out.println("student1 equals student3? " + student1.equals(student3));

        System.out.println("\nAll persons in the system:");
        Person[] allPersons = Person.getAllPersons();
        for (Person p : allPersons) {
            System.out.println(" - " + p.getName() + " (" + p.getDateOfBirth() + ")");
        }

        if (student1.getAssignedProject() != null) {
            System.out.println("\n" + student1.getName() + " has been assigned: " 
                    + student1.getAssignedProject().getTitle());
        } else {
            System.out.println("\n" + student1.getName() + " has no assigned project.");
        }
    }
}
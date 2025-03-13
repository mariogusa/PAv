public class Student extends Person {
    private String registrationNumber;
    private Project assignedProject;

    public Student(String name, String dateOfBirth, String registrationNumber) {
        super(name, dateOfBirth);
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setAssignedProject(Project project) {
        this.assignedProject = project;
    }

    public Project getAssignedProject() {
        return assignedProject;
    }
}

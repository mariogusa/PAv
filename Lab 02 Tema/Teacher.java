public class Teacher extends Person {
    private Project[] proposedProjects;
    private int projectCount = 0;

    public Teacher(String name, String dateOfBirth, int maxProjects) {
        super(name, dateOfBirth);
        this.proposedProjects = new Project[maxProjects];
    }

    public void addProject(Project project) {
        if (projectCount < proposedProjects.length) {
            proposedProjects[projectCount++] = project;
        } else {
            System.out.println("Cannot add more projects. Maximum reached.");
        }
    }
    
    public Project[] getProposedProjects() {
        Project[] result = new Project[projectCount];
        for (int i = 0; i < projectCount; i++) {
            result[i] = proposedProjects[i];
        }
        return result;
    }
}

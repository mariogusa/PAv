public class ProjectAggregator {

    public static Project assignProjectToStudent(Teacher teacher, Student student, String projectTitle) {
        Project[] teacherProjects = teacher.getProposedProjects();
        for (Project p : teacherProjects) {
            if (p.getTitle().equals(projectTitle)) {
                student.setAssignedProject(p);
                System.out.println("Assigned project \"" + p.getTitle() 
                        + "\" to student " + student.getName());
                return p;
            }
        }
        System.out.println("No project with title \"" + projectTitle + "\" found for teacher " 
                + teacher.getName());
        return null;
    }
}

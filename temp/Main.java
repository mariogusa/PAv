public class Main {
    public static void main(String[] args) {
        Student student = new Student(1, "Mario");

        Project project = new Project("Artificial Intelligence", ProjectType.THEORETICAL);

        System.out.println(student);
        System.out.println(project);
    }
}
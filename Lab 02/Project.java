public class Project {
    private String title;
    private ProjectType type;

    public Project(String title, ProjectType type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public ProjectType getType() {
        return type;
    }
    
    public void setType(ProjectType type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return "Project{" + "title='" + title + '\'' + ", type=" + type + '}';
    }
}
package app.taskLogic.exceptions;

public class ProjectNotFoundException extends RuntimeException {

    private Long projectId;

    public ProjectNotFoundException(String message) {
        super(message);
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }
}

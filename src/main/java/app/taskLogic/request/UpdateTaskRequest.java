package app.taskLogic.request;

import app.taskLogic.models.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTaskRequest {
    @NotBlank
    String header;

    @NotBlank
    String description;


    TaskPriority priority;


    Long projectId;
}
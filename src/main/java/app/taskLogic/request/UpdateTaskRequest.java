package app.taskLogic.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTaskRequest {
    @NotBlank
    String description;
}
package app.taskLogic.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddTaskRequest {
    @NotBlank
    Long user_id;
    @NotBlank
    String description;
}

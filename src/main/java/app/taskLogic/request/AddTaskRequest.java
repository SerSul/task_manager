package app.taskLogic.request;

import app.taskLogic.models.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class AddTaskRequest {

    @NotBlank
    String header;

    @NotBlank
    String description;

    @NotBlank
    TaskPriority priority;




}

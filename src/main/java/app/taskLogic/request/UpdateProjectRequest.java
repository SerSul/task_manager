package app.taskLogic.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateProjectRequest {

    @NotBlank
    private String name;
}

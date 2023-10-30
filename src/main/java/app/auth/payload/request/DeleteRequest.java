package app.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteRequest {
    @NotBlank
    private Long id;
}

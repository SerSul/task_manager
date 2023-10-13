package app.taskLogic.models;

import app.auth.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.management.ConstructorParameters;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;
    private String description;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskPriority priority;


}

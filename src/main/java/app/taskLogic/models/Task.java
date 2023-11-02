package app.taskLogic.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private Long user_id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;
}

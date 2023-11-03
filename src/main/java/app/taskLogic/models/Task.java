package app.taskLogic.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "userId")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskPriority priority;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}

package app.taskLogic.models;

import app.auth.models.User;
import jakarta.persistence.*;
import lombok.*;

import javax.management.ConstructorParameters;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(name = "user_id") // Создаем столбец user_id
    private Long userId; // Поле для хранения user_i
}

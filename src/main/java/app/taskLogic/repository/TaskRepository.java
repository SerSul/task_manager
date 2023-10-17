package app.taskLogic.repository;


import app.auth.models.ERole;
import app.taskLogic.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findAllById(Long Id);

    @Query("SELECT t.user_id FROM Task t WHERE t.id = ?1")
    Long findUserIdByTaskId(Long taskId);

    @Query("SELECT t FROM Task t WHERE t.user_id = :userId")
    List<Task> findTasksByUserId(@Param("userId") Long userId);


}

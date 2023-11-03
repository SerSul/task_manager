package app.taskLogic.repository;

import app.taskLogic.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> getTasksByUserId(Long Id);

    @Query("SELECT t.userId FROM Task t WHERE t.id = ?1")
    Long findUserIdByTaskId(Long taskId);



}

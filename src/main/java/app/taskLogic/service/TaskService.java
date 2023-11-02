package app.taskLogic.service;

import app.taskLogic.models.Task;
import app.taskLogic.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }



    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getallTasks(Long userId)
    {
        return taskRepository.findTasksByUserId(userId);
    }

    public Task updateTask(Task task) {
        taskRepository.save(task);
        return task;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Long getUserIdByTaskId(Long taskId) {
        return taskRepository.findUserIdByTaskId(taskId);
    }
}

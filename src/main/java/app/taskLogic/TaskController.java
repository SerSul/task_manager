package app.taskLogic;

import app.auth.payload.response.MessageResponse;
import app.taskLogic.models.Task;
import app.taskLogic.request.AddTaskRequest;
import app.taskLogic.request.UpdateTaskRequest;
import app.taskLogic.service.TaskService;
import app.auth.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


/*
 * {
 * "description":
 * "id": получается из токена
 * }
 *
 * */

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtils jwtUtils;

    @CrossOrigin(origins = "*")
    @PostMapping("/createTask")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTask(
            @Valid @RequestBody AddTaskRequest addTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        if (!(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {
            return ResponseEntity.badRequest().body("Вы не авторизованы");
        }

        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Task task = new Task();
        task.setDescription(addTaskRequest.getDescription());
        task.setUserId(jwtUtils.getUserIdFromJwtToken(jwtToken));
        Task createdTask = taskService.createTask(task);

        return ResponseEntity.ok(createdTask);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteTask/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long taskId,
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        if (!(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {
            return ResponseEntity.badRequest().body("Вы не авторизованы");
        }

        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Long userIdFromToken = jwtUtils.getUserIdFromJwtToken(jwtToken);
        Long userIdByTaskId = taskService.getUserIdByTaskId(taskId);

        if (Objects.equals(userIdFromToken, userIdByTaskId)) {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok("Задача успешно удалена");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Задача не найдена"));
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateTask/{taskId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest updateTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        if (!(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {
            return ResponseEntity.badRequest().body("Вы не авторизованы");
        }

        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Long userIdFromToken = jwtUtils.getUserIdFromJwtToken(jwtToken);
        Long userIdByTaskId = taskService.getUserIdByTaskId(taskId);

        if (Objects.equals(userIdFromToken, userIdByTaskId)) {
            Task taskToUpdate = taskService.getTaskById(taskId).orElse(null);

            if (taskToUpdate != null) {
                taskToUpdate.setDescription(updateTaskRequest.getDescription());
                taskService.updateTask(taskToUpdate);
                return ResponseEntity.ok("Задача успешно обновлена");
            } else {
                return ResponseEntity.badRequest().body("Задача не найдена");
            }
        } else {
            return ResponseEntity.badRequest().body("У вас нет прав на обновление этой задачи");
        }
    }
}

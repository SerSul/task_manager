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

import java.util.List;
import java.util.Objects;


/*
 *{
    "header": "String",
    "description": "String",
    "priority": "String",
    "deadline":"String" Format: dd.mm.yyyy

}
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createTask(
            @Valid @RequestBody AddTaskRequest addTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        if (!(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {
            return ResponseEntity.badRequest().body("Вы не авторизованы");
        }

        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Task task = new Task();
        task.setHeader(addTaskRequest.getHeader());
        task.setDescription(addTaskRequest.getDescription());
        task.setUser_id(jwtUtils.getUserIdFromJwtToken(jwtToken));
        task.setPriority(addTaskRequest.getPriority());
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')" )
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
                taskToUpdate.setHeader(updateTaskRequest.getHeader());
                taskToUpdate.setDescription(updateTaskRequest.getDescription());
                taskToUpdate.setUser_id(jwtUtils.getUserIdFromJwtToken(jwtToken));

                if (updateTaskRequest.getPriority() != null) {
                    taskToUpdate.setPriority(updateTaskRequest.getPriority());
                }
                Task updatefddTask = taskService.updateTask(taskToUpdate);
                return ResponseEntity.ok(updatefddTask);
            } else {
                return ResponseEntity.badRequest().body("Задача не найдена");
            }
        } else {
            return ResponseEntity.badRequest().body("У вас нет прав на обновление этой задачи");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getTasks")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTasks(
            @RequestHeader(value = "Authorization") String authorizationHeader)
    {
        if (!(authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {
            return ResponseEntity.badRequest().body("Вы не авторизованы");
        }

        String jwtToken = authorizationHeader.replace("Bearer ", "");
        Long userIdFromToken = jwtUtils.getUserIdFromJwtToken(jwtToken);

        return ResponseEntity.ok(taskService.getallTasks(userIdFromToken));
    }

}

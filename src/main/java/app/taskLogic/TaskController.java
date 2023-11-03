package app.taskLogic;

import app.auth.payload.response.MessageResponse;

import app.taskLogic.models.Project;
import app.taskLogic.models.Task;
import app.taskLogic.request.AddTaskRequest;
import app.taskLogic.request.UpdateTaskRequest;

import app.taskLogic.service.ProjectServise;
import app.taskLogic.service.TaskService;
import app.auth.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "JWT")
@PreAuthorize("isAuthenticated()")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    ProjectServise projectService;
    @Autowired
    private JwtUtils jwtUtils;

    // Метод для создания ResponseEntity с указанным телом, статусом и сообщением статуса
    private ResponseEntity<?> createJsonResponse(Object body, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("data", body);
        return new ResponseEntity<>(response, status);
    }

    // Метод для возврата ResponseEntity с кодом ошибки 400 (BAD_REQUEST)
    private ResponseEntity<?> badRequestJsonResponse(Object body) {
        return createJsonResponse(body, HttpStatus.BAD_REQUEST);
    }

    // Метод для возврата ResponseEntity с кодом успешного выполнения (200 OK)
    private ResponseEntity<?> okJsonResponse(Object body) {
        return createJsonResponse(body, HttpStatus.OK);
    }

    // Метод для извлечения идентификатора пользователя из JWT-токена
    private Long getUserIdFromToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.replace("Bearer ", "");
            return jwtUtils.getUserIdFromJwtToken(jwtToken);
        }
        return null;
    }

    // Метод для обработки POST-запроса и создания задачи
    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(
            @Valid @RequestBody AddTaskRequest addTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);

        Task task = new Task();
        task.setHeader(addTaskRequest.getHeader());
        task.setDescription(addTaskRequest.getDescription());
        task.setUserId(userId);
        task.setPriority(addTaskRequest.getPriority());

        Long projectId = addTaskRequest.getProjectId();
        if (projectId!=null) {
            Project project = projectService.getProjectById(projectId).orElse(null);
            task.setProject(project);
        }
        Task createdTask = taskService.createTask(task);
        return okJsonResponse(createdTask);
    }

    // Метод для обработки DELETE-запроса и удаления задачи по идентификатору
    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long taskId,
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);

        Long userIdByTaskId = taskService.getUserIdByTaskId(taskId);

        if (Objects.equals(userId, userIdByTaskId)) {
            taskService.deleteTask(taskId);
            return okJsonResponse("Задача успешно удалена");
        } else {
            return badRequestJsonResponse(new MessageResponse("Задача не найдена"));
        }
    }

    // Метод для обработки POST-запроса и обновления задачи по идентификатору
    @PostMapping("/updateTask/{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest updateTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);
        Long userIdByTaskId = taskService.getUserIdByTaskId(taskId);

        if (Objects.equals(userId, userIdByTaskId)) {
            Task taskToUpdate = taskService.getTaskById(taskId).orElse(null);

            if (taskToUpdate != null) {
                if (updateTaskRequest.getHeader() != null) {
                    taskToUpdate.setHeader(updateTaskRequest.getHeader());
                }
                if (updateTaskRequest.getDescription() != null) {
                    taskToUpdate.setDescription(updateTaskRequest.getDescription());
                }
                if (updateTaskRequest.getPriority() != null) {
                    taskToUpdate.setPriority(updateTaskRequest.getPriority());
                }
                if (updateTaskRequest.getProjectId() != null) {
                    Long projectId = updateTaskRequest.getProjectId();
                    Project project = projectService.getProjectById(projectId).orElse(null);
                    taskToUpdate.setProject(project);
                }

                Task updatedTask = taskService.updateTask(taskToUpdate);
                return okJsonResponse(updatedTask);
            } else {
                return badRequestJsonResponse("Задача не найдена");
            }
        } else {
            return badRequestJsonResponse("У вас нет прав на обновление этой задачи");
        }
    }


    // Метод для обработки GET-запроса и получения списка задач
    @GetMapping("/getTasks")
    public ResponseEntity<?> getTasks(
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);

        List<Task> userTasks = taskService.getAllTasks(userId);
        return okJsonResponse(userTasks);
    }
}

package app.taskLogic;

import app.auth.payload.response.MessageResponse;
import app.taskLogic.models.Task;
import app.taskLogic.request.AddTaskRequest;
import app.taskLogic.request.UpdateTaskRequest;
import app.taskLogic.service.TaskService;
import app.auth.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@SecurityRequirement(name = "JWT")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtils jwtUtils;

    private ResponseEntity<?> createResponseEntity(Object body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<?> unauthorizedResponse() {
        return createResponseEntity("Вы не авторизованы", HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<?> badRequestResponse(Object body) {
        return createResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> okResponse(Object body) {
        return createResponseEntity(body, HttpStatus.OK);
    }

    private Long getUserIdFromToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.replace("Bearer ", "");
            return jwtUtils.getUserIdFromJwtToken(jwtToken);
        }
        return null;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/createTask")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createTask(
            @Valid @RequestBody AddTaskRequest addTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        Long userId = getUserIdFromToken(authorizationHeader);



        Task task = new Task();
        task.setHeader(addTaskRequest.getHeader());
        task.setDescription(addTaskRequest.getDescription());
        task.setUser_id(userId);
        task.setPriority(addTaskRequest.getPriority());

        Task createdTask = taskService.createTask(task);
        return okResponse(createdTask);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteTask/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long taskId,
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        Long userId = getUserIdFromToken(authorizationHeader);


        Long userIdByTaskId = taskService.getUserIdByTaskId(taskId);

        if (Objects.equals(userId, userIdByTaskId)) {
            taskService.deleteTask(taskId);
            return okResponse("Задача успешно удалена");
        } else {
            return badRequestResponse(new MessageResponse("Задача не найдена"));
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateTask/{taskId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest updateTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        Long userId = getUserIdFromToken(authorizationHeader);


        Long userIdByTaskId = taskService.getUserIdByTaskId(taskId);

        if (Objects.equals(userId, userIdByTaskId)) {
            Task taskToUpdate = taskService.getTaskById(taskId).orElse(null);

            if (taskToUpdate != null) {
                taskToUpdate.setHeader(updateTaskRequest.getHeader());
                taskToUpdate.setDescription(updateTaskRequest.getDescription());
                taskToUpdate.setUser_id(userId);

                if (updateTaskRequest.getPriority() != null) {
                    taskToUpdate.setPriority(updateTaskRequest.getPriority());
                }
                Task updatedTask = taskService.updateTask(taskToUpdate);
                return okResponse(updatedTask);
            } else {
                return badRequestResponse("Задача не найдена");
            }
        } else {
            return badRequestResponse("У вас нет прав на обновление этой задачи");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getTasks")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTasks(
            @RequestHeader(value = "Authorization") String authorizationHeader) {

        Long userId = getUserIdFromToken(authorizationHeader);


        List<Task> userTasks = taskService.getallTasks(userId);
        return okResponse(userTasks);
    }
}

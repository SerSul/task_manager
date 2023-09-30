package app.taskLogic;

import app.auth.models.User;

import app.auth.payload.response.MessageResponse;
import app.auth.repository.UserRepository;
import app.taskLogic.models.Task;
import app.taskLogic.service.TaskService;
import app.taskLogic.service.*;
import app.taskLogic.request.AddTaskRequest;
import app.auth.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;
    @Autowired
    JwtUtils jwtUtils = new JwtUtils();
    @Autowired
    UserRepository userRepository;

    @CrossOrigin(origins = "*")
    @PostMapping("/createTask")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTask(
            @Valid @RequestBody AddTaskRequest addTaskRequest,
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.replace("Bearer ", "");
            String description = addTaskRequest.getDescription();
            Task task = new Task();
            task.setDescription(addTaskRequest.getDescription());
            task.setUserId(jwtUtils.getUserIdFromJwtToken(jwtToken));
            Task createdTask = taskService.createTask(task);

            return ResponseEntity.ok(createdTask);
        }
        return null;
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/deletetask/{taskId}")
    public ResponseEntity<?> deleteTask(
            @PathVariable Long taskId,
            @RequestHeader(value = "Authorization") String authorizationHeader) {
        {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwtToken = authorizationHeader.replace("Bearer ", "");
                Long userIdFromToken = jwtUtils.getUserIdFromJwtToken(jwtToken);
                Long userIdByTaskId = taskService.getUserIdByTaskId(taskId);
                if (Objects.equals(userIdFromToken, userIdByTaskId))
                {
                    taskService.deleteTask(taskId);
                    return ResponseEntity.ok("Задача успешно удалена");
                }
                else {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Задача не найдена"));
                }
            }

            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Вы не авторизованы");
            }


        }

    }
}
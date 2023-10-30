package app.taskLogic;

import app.auth.security.jwt.JwtUtils;
import app.taskLogic.models.Task;
import app.taskLogic.models.TaskPriority;
import app.taskLogic.request.AddTaskRequest;
import app.taskLogic.request.UpdateTaskRequest;
import app.taskLogic.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Mock
    private JwtUtils jwtUtils;

    @Before
    public void setUp() {
        lenient().when(jwtUtils.getUserIdFromJwtToken(anyString())).thenReturn(1L);
    }


    @Test
    public void testCreateTaskSuccess() {
        AddTaskRequest addTaskRequest = new AddTaskRequest();
        addTaskRequest.setHeader("Task Header");
        addTaskRequest.setDescription("Task Description");
        addTaskRequest.setPriority(TaskPriority.HIGH);

        // Mock the necessary behavior, including authorization and task creation
        when(jwtUtils.getUserIdFromJwtToken(anyString())).thenReturn(1L);
        when(taskService.createTask(any())).thenReturn(new Task(/* Provide necessary details */));

        ResponseEntity<?> response = taskController.createTask(addTaskRequest, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfaWQiOjEsImlhdCI6MTY5ODY5NjM2MywiZXhwIjoxNjk4NzgyNzYzfQ.yc31VD_8AHkGSv-JasnLY7yZGQHtpuGyR8l9kTCNlCM");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions for the response body if needed
    }



    @Test
    public void testCreateTaskUnauthorized() {
        AddTaskRequest addTaskRequest = new AddTaskRequest();

        ResponseEntity<?> response = taskController.createTask(addTaskRequest, "invalid_token");

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testDeleteTaskSuccess() {
        when(taskService.getUserIdByTaskId(anyLong())).thenReturn(1L);

        ResponseEntity<?> response = taskController.deleteTask(1L, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfaWQiOjEsImlhdCI6MTY5ODY5NjM2MywiZXhwIjoxNjk4NzgyNzYzfQ.yc31VD_8AHkGSv-JasnLY7yZGQHtpuGyR8l9kTCNlCM");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteTaskUnauthorized() {

        ResponseEntity<?> response = taskController.deleteTask(1L, "invalid_token");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testDeleteTaskNoPermission() {
        // Mock the necessary behavior, including authorization and user/task association
        when(jwtUtils.getUserIdFromJwtToken(anyString())).thenReturn(1L);
        when(taskService.getUserIdByTaskId(anyLong())).thenReturn(2L);  // Different user ID

        ResponseEntity<?> response = taskController.deleteTask(1L, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfaWQiOjEsImlhdCI6MTY5ODY5NjM2MywiZXhwIjoxNjk4NzgyNzYzfQ.yc31VD_8AHkGSv-JasnLY7yZGQHtpuGyR8l9kTCNlCM");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void testUpdateTaskUnauthorized() {
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();

        ResponseEntity<?> response = taskController.updateTask(1L, updateTaskRequest, "invalid_token");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testUpdateTaskNotFound() {
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        when(taskService.getUserIdByTaskId(anyLong())).thenReturn(1L);
        when(taskService.getTaskById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> response = taskController.updateTask(1L, updateTaskRequest, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfaWQiOjEsImlhdCI6MTY5ODY5NjM2MywiZXhwIjoxNjk4NzgyNzYzfQ.yc31VD_8AHkGSv-JasnLY7yZGQHtpuGyR8l9kTCNlCM");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetTasksSuccess() {
        when(taskService.getallTasks(anyLong())).thenReturn(List.of(new Task()));

        ResponseEntity<?> response = taskController.getTasks("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJfaWQiOjEsImlhdCI6MTY5ODY5NjM2MywiZXhwIjoxNjk4NzgyNzYzfQ.yc31VD_8AHkGSv-JasnLY7yZGQHtpuGyR8l9kTCNlCM");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetTasksUnauthorized() {
        ResponseEntity<?> response = taskController.getTasks("invalid_token");
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}

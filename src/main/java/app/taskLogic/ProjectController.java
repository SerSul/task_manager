package app.taskLogic;




import app.auth.security.jwt.JwtUtils;
import app.taskLogic.models.Project;
import app.taskLogic.models.Task;
import app.taskLogic.request.CreateProjectRequest;
import app.taskLogic.service.ProjectService;
import app.taskLogic.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@SecurityRequirement(name = "JWT")
@RequestMapping("/projects")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin(origins = "*")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    TaskService taskService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/create")

    public ResponseEntity<Project> createProject(@RequestBody CreateProjectRequest projectRequest) {
        try {
            Project project = new Project();
            project.setName(projectRequest.getName());
            Project createdProject = projectService.createProject(project);
            return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {
        try {
            Project project = projectService.getProjectById(projectId).orElse(null);
            if (project == null) {
                return new ResponseEntity<>("Проект не найден", HttpStatus.NOT_FOUND);
            }

            List<Task> fds = new ArrayList<Task>() ;

            projectService.deleteProject(projectId);
            return new ResponseEntity<>("Проект удален", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

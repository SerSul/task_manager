package app.taskLogic;




import app.auth.security.jwt.JwtUtils;
import app.taskLogic.models.Project;
import app.taskLogic.request.CreateProjectRequest;
import app.taskLogic.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtUtils jwtUtils;

    // Создание нового проекта
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {

        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }


    @DeleteMapping("/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId,
    @RequestHeader(value = "Authorization") String authorizationHeader

    ) {
        // Возможно, вам нужно провести аутентификацию пользователя и проверить его права доступа
        // В этом примере, просто передаем идентификатор проекта сервису для удаления
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

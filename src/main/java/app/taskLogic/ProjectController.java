package app.taskLogic;




import app.auth.security.jwt.JwtUtils;
import app.taskLogic.request.CreateProjectRequest;
import app.taskLogic.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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


//    @PostMapping("/createProject")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public ResponseEntity<?> createProject(
//            @Valid @RequestBody CreateProjectRequest createProjectRequest,
//            @RequestHeader(value = "Authorization") String authorizationHeader) {
//        // Ваша логика для создания нового проекта
//        // Используйте информацию из createProjectRequest
//        // и идентификатор пользователя из JWT токена
//        // Верните созданный проект или сообщение об ошибке
//        return ;
//    }

}

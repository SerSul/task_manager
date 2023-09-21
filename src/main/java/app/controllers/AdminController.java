package app.controllers;

import app.models.ERole;
import app.models.Role;
import app.payload.request.DeleteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import app.models.User;

import app.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @PostMapping("/deleteuser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteuser(@Valid @RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();

        User user = userRepository.findUserById(id);
        if (user == null) {
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }

        for (Role role : user.getRoles()) {
            if (ERole.ROLE_ADMIN.equals(role.getName())) {
                return ResponseEntity.badRequest().body("Вы пытаетесь удалить пользователя с правами администратора");
            }
        }
        userRepository.delete(user);

        return ResponseEntity.ok("Пользователь успешно удален");
    }


}

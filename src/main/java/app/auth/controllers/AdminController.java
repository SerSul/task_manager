package app.auth.controllers;

import app.auth.payload.request.DeleteRequest;
import app.auth.repository.UserRepository;
import app.auth.models.ERole;
import app.auth.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import app.auth.models.User;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @CrossOrigin(origins = "*")
    @PostMapping("/deleteuser")
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
    @CrossOrigin(origins = "*")
    @GetMapping( "/getallusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            User filteredUser = new User(user.getId(), user.getEmail(), user.getUsername(), user.getRoles());
            filteredUsers.add(filteredUser);
        }

        return ResponseEntity.ok(filteredUsers);
    }


}

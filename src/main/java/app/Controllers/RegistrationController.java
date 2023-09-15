package app.Controllers;

import app.Entity.User;
import app.servises.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка валидации");
        }

        // Попытка сохранить пользователя в базу данных
        boolean isUserCreated = userService.saveUser(user);

        if (!isUserCreated) {
            // Если пользователь с таким именем уже существует
            bindingResult.rejectValue("username", "error.user", "Пользователь с таким именем уже существует");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь уже существует");
        }

        // Если пользователь успешно зарегистрирован
        return ResponseEntity.ok("Регистрация успешно завершена");
    }
}

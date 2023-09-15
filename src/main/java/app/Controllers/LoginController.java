package app.Controllers;

import app.Entity.User;
import app.interfaces.UserRepository;
import app.servises.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        // Выполните аутентификацию с использованием authenticationService
        boolean authenticated = authenticationService.login(user.getUsername(), user.getPassword());

        if (authenticated) {
            User authenticatedUser = userRepository.findByUsername(user.getUsername());
            return ResponseEntity.ok("User authenticated: " + authenticatedUser.getUsername());
        } else {
            // Аутентификация не удалась, верните статус ошибки и сообщение
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}

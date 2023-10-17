package app.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class allTest {

    @GetMapping("/test")
    public ResponseEntity<?> testAll()
    {
        return ResponseEntity.ok("all auth");
    }

    @GetMapping("/testUser")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<?> testUser()
    {
        return ResponseEntity.ok("User auth");
    }

    @GetMapping("/testAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> testAdmin()
    {
        return ResponseEntity.ok("Admin auth");
    }
}

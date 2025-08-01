package test.business.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.business.model.LoginRequest;
import test.business.model.RegisterRequest;
import test.business.model.User;
import test.business.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return userService.register(request.getUsername(), request.getPassword())
                ? ResponseEntity.ok("注册成功")
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request.getUsername(), request.getPassword())
                ? ResponseEntity.ok("登录成功")
                : ResponseEntity.status(401).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsersExcept(@RequestParam String username) {
        return ResponseEntity.ok(userService.getAllUsersExcept(username));
    }
}

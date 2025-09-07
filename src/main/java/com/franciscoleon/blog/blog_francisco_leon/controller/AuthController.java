package com.franciscoleon.blog.blog_francisco_leon.controller;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.LoginDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.RegisterDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // AUN NO SE TIENE SEGURIDAD JWT, ES POSIBLE QUE SE DEBAN HACER CAMBIOS AQUI O AUTHSERVICE

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private <T> ResponseEntity<Map<String, Object>> createResponse(HttpStatus status, String message, T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("data", data);
        return ResponseEntity.status(status).body(body);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterDTO request) {
        try {
            User newUser = authService.register(request.getUsername(), request.getEmail(), request.getPassword());
            Map<String, Object> data = new HashMap<>();
            data.put("username", newUser.getUsername());
            data.put("email", newUser.getEmail());
            return createResponse(HttpStatus.CREATED, "Usuario registrado con éxito", data);
        } catch (RuntimeException e) {
            return createResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO request) {
        boolean success = authService.login(request.getUsername(), request.getPassword());

        if (success) {
            User user = authService.findByUsername(request.getUsername());
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("role", user.getRole().name());
            return createResponse(HttpStatus.OK, "Login correcto", data);
        } else {
            return createResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", null);
        }
    }
}

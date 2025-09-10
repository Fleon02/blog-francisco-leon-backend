package com.franciscoleon.blog.blog_francisco_leon.controller;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.LoginDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.security.JwtService;
import com.franciscoleon.blog.blog_francisco_leon.service.AuthService;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    

    @Autowired
    private JwtService jwtService;

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

    /**
     * Autenticación de usuario
     *
     * @param request Contiene el email y password para autenticar al usuario
     * @return Un objeto con la información del usuario autenticado y el token JWT
     *         en caso de que la autenticación sea correcta, o un objeto con el
     *         mensaje de error en caso de que la autenticación sea incorrecta
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO request) {
        User user = authService.login(request.getEmail(), request.getPassword());

        if (user != null) {
            // 🔑 Generamos el token JWT usando JwtService
            String token = jwtService.generarToken(user);

            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("role", user.getRole().name());
            data.put("token", token);

            return createResponse(HttpStatus.OK, "Login correcto", data);
        } else {
            return createResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", null);
        }
    }

    // Endpoint para validar token
    // Por ahora esto, en un futuro a lo mejor se pone REFRESH TOKEN QUE SE
    // GUARDARIA EN LA BBDD

    /**
     * Valida un token JWT pasado en el header "Authorization" en formato "Bearer
     * <token>".
     * Devuelve un objeto JSON con una propiedad "valid" que indica si el token es
     * válido o no.
     * 
     * @param header El valor del header "Authorization"
     * @return Un objeto JSON con la propiedad "valid"
     */

    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String header) {
        Map<String, Object> response = new HashMap<>();

        if (header == null || !header.startsWith("Bearer ")) {
            response.put("valid", false);
            response.put("error", "Missing or invalid Authorization header");
            return ResponseEntity.ok(response);
        }

        String token = header.substring(7);

        if (jwtService.esTokenValido(token)) {
            Claims claims = jwtService.obtenerClaims(token);

            response.put("valid", true);
            response.put("email", claims.getSubject());
            response.put("roles", claims.get("roles"));
            response.put("expiresAt", claims.getExpiration());

        } else {
            response.put("valid", false);
            response.put("error", "Token is expired or invalid");
            
        }

        return ResponseEntity.ok(response);
    }

}

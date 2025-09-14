package com.franciscoleon.blog.blog_francisco_leon.controller;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.LoginDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.security.JwtService;
import com.franciscoleon.blog.blog_francisco_leon.service.AuthService;

import io.jsonwebtoken.Claims;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth-cookie")
public class AuthCookieController {

    private static final Logger log = LoggerFactory.getLogger(AuthCookieController.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Environment env; // Para leer el profile activo

    private final AuthService authService;

    public AuthCookieController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Crea un objeto ResponseEntity con un body que contiene los atributos
     * "status", "message" y "data".
     *
     * @param status  Estado de la respuesta HTTP
     * @param message Mensaje de la respuesta
     * @param data    Datos de la respuesta
     * @return El objeto ResponseEntity con el body configurado
     */
    private <T> ResponseEntity<Map<String, Object>> createResponse(HttpStatus status, String message, T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("data", data);
        return ResponseEntity.status(status).body(body);
    }

    /**
     * Autentica un usuario y establece una cookie HTTPOnly con un token JWT
     * que contiene la información del usuario autenticado.
     *
     * <p>
     * La cookie se establece con el parámetro "Secure" en true en modo
     * producción (desplegado en Render o Netlify) y false en modo desarrollo
     * (localhost). En ambos casos, se establece el parámetro "HttpOnly" en
     * true.
     *
     * @param request  Credenciales del usuario
     * @param response Respuesta HTTP
     * @return Un objeto ResponseEntity con un body que contiene los atributos
     *         "status", "message" y "data". El atributo "data" contiene un
     *         objeto con los atributos "username", "email" y "role" del usuario
     *         autenticado.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDTO request,
            HttpServletRequest httpRequest,
            HttpServletResponse response) {
        // Logs de debugging
        String origin = httpRequest.getHeader("Origin");
        String userAgent = httpRequest.getHeader("User-Agent");
        String host = httpRequest.getHeader("Host");

        log.info("=== DEBUG COOKIE ===");
        log.info("[DEBUG] Origin: {}", origin);
        log.info("[DEBUG] Host: {}", host);
        log.info("[DEBUG] User-Agent: {}", userAgent);
        log.info("[DEBUG] Request URL: {}", httpRequest.getRequestURL());

        User user = authService.login(request.getEmail(), request.getPassword());
        if (user == null) {
            return createResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", null);
        }

        String token = jwtService.generarToken(user);
        String activeProfile = Arrays.stream(env.getActiveProfiles()).findFirst().orElse("dev");

        log.info("[DEBUG] Active Profile: {}", activeProfile);
        log.info("[DEBUG] Token generado: {}", token.substring(0, 20) + "...");

        if (activeProfile.equals("dev")) {
            log.info("[DEBUG] Configurando cookie para desarrollo");

            // Método 2: Header Set-Cookie manual (más control)
            String setCookieHeader = String.format(
                    "token=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax",
                    token, 24 * 60 * 60);
            response.addHeader("Set-Cookie", setCookieHeader);
            log.info("[DEBUG] Header Set-Cookie agregado: {}", setCookieHeader);

        } else {
           log.info("[DEBUG] Configurando cookie para producción");

            // MÉTODO 2: También header manual como backup
            String setCookieHeader = String.format(
                    "token=%s; Max-Age=%d; Path=/; Secure; HttpOnly; SameSite=None;",
                    token, 24 * 60 * 60);
            response.addHeader("Set-Cookie", setCookieHeader);

            // Headers CORS muy específicos
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Vary", "Origin");

            log.info("[DEBUG] Cookie addCookie() configurada");
            log.info("[DEBUG] Header Set-Cookie manual: {}", setCookieHeader);
            log.info("[DEBUG] Origin permitido: {}", origin);
        }

        // Verificar que el header se estableció
        Collection<String> cookieHeaders = response.getHeaders("Set-Cookie");
        log.info("[DEBUG] Headers Set-Cookie en response: {}", cookieHeaders);
        log.info("[DEBUG] Todos los headers: {}", response.getHeaderNames());

        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("role", user.getRole().name());

        log.info("=== FIN DEBUG COOKIE ===");
        return createResponse(HttpStatus.OK, "Login correcto con cookie", data);
    }

    /**
     * Verifica si el usuario tiene una cookie de autenticación válida.
     *
     * <p>
     * La respuesta contiene un atributo "valid" que indica si la cookie es
     * válida o no. Si la cookie es válida, se incluye un atributo "data" que
     * contiene el email del usuario autenticado.
     *
     * @param token Cookie de autenticación (opcional)
     * @return Un objeto ResponseEntity con un body que contiene los atributos
     *         "valid" y "data".
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@CookieValue(value = "token", required = false) String token) {
        Map<String, Object> response = new HashMap<>();

        if (token != null && jwtService.esTokenValido(token)) {
            Map<String, Object> data = new HashMap<>();
            data.put("email", jwtService.obtenerEmailDelToken(token));
            response.put("valid", true);
            response.put("data", data);
        } else {
            response.put("valid", false);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Cierra la sesión actual y borra la cookie de autenticación.
     *
     * <p>
     * La respuesta contiene un atributo "message" con el valor "Sesión
     * cerrada".
     *
     * @param response Objeto HttpServletResponse para borrar la cookie
     * @return Un objeto ResponseEntity con un body que contiene el atributo
     *         "message".
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {

        log.info("Logout");

        String activeProfile = Arrays.stream(env.getActiveProfiles()).findFirst().orElse("dev");
        log.info("[DEBUG] Logout - Active Profile: {}", activeProfile);

        if (activeProfile.equals("dev")) {
            log.info("[DEBUG] Configurando logout cookie para desarrollo");

            // Header Set-Cookie manual para desarrollo
            String setCookieHeader = "token=; Max-Age=0; Path=/; HttpOnly; SameSite=Lax";
            response.addHeader("Set-Cookie", setCookieHeader);
            log.info("[DEBUG] Header logout Set-Cookie agregado: {}", setCookieHeader);

        } else {
            log.info("[DEBUG] Configurando logout cookie para producción");

            // Header Set-Cookie manual para producción
            String setCookieHeader = "token=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=None;";
            response.addHeader("Set-Cookie", setCookieHeader);
            log.info("[DEBUG] Header logout Set-Cookie agregado: {}", setCookieHeader);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("message", "Sesión cerrada");
        return ResponseEntity.ok(res);
    }

    /**
     * Verifica si el token JWT proporcionado en la cookie de autenticación
     * es válido.
     *
     * <p>
     * La respuesta contiene un atributo "valid" que indica si el token es
     * válido o no. Si el token es válido, se incluyen atributos "email",
     * "roles" y "expiresAt" con la información del usuario autenticado.
     *
     * @param cookieToken Token JWT proporcionado en la cookie de
     *                    autenticación (opcional)
     * @return Un objeto ResponseEntity con un body que contiene los atributos
     *         "valid", "email", "roles" y "expiresAt".
     */
    @GetMapping("/validate-token-cookie")
    public ResponseEntity<Map<String, Object>> validateTokenCookie(
            @CookieValue(value = "token", required = false) String cookieToken) {

        Map<String, Object> response = new HashMap<>();

        if (cookieToken == null) {
            response.put("valid", false);
            response.put("error", "No token provided in cookie");
            return ResponseEntity.ok(response);
        }

        if (jwtService.esTokenValido(cookieToken)) {
            Claims claims = jwtService.obtenerClaims(cookieToken);
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

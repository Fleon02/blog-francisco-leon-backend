package com.franciscoleon.blog.blog_francisco_leon.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.franciscoleon.blog.blog_francisco_leon.security.JwtService;
import com.franciscoleon.blog.blog_francisco_leon.security.TokenJwtConfig;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // muchos logs para depuración, descomentar si es necesario

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtService jwtService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // String requestUri = request.getRequestURI();
        // String method = request.getMethod();
        // log.info("=== PROCESANDO SOLICITUD: {} {} ===", method, requestUri);

        String token = extractToken(request);

        if (token != null) {
            // log.info("Token encontrado, longitud: {}", token.length());
            // log.debug("Token: {}", token.substring(0, Math.min(50, token.length())) + "...");
            
            try {
                if (jwtService.esTokenValido(token)) {
                    // log.info("Token es válido, extrayendo información...");
                    
                    String email = jwtService.obtenerEmailDelToken(token);
                    // log.info("Email extraído del token: {}", email);
                    
                    Claims claims = jwtService.obtenerClaims(token);
                    // log.info("Claims obtenidos: {}", claims.keySet());

                    @SuppressWarnings("unchecked")
                    List<String> roles = claims.get("roles", List.class);
                    // log.info("Roles extraídos: {}", roles);

                    if (roles != null && !roles.isEmpty()) {
                        List<GrantedAuthority> authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority(role))
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        // log.info("✓ USUARIO AUTENTICADO EXITOSAMENTE: {} con authorities: {}", email, authorities);
                    } else {
                        // log.warn("✗ Token válido pero SIN ROLES para usuario: {}", email);
                    }
                } else {
                    // log.warn("✗ Token INVÁLIDO o expirado");
                }
            } catch (Exception e) {
                // log.error("✗ ERROR procesando token JWT: {}", e.getMessage(), e);
                SecurityContextHolder.clearContext();
            }
        } else {
            // log.warn("✗ NO SE ENCONTRÓ TOKEN en la solicitud para: {}", requestUri);
            // // Mostrar headers disponibles para debug
            // log.debug("Headers disponibles:");
            request.getHeaderNames().asIterator().forEachRemaining(headerName -> 
                log.debug("  {}: {}", headerName, request.getHeader(headerName)));
            
            // Mostrar cookies disponibles para debug
            if (request.getCookies() != null) {
                log.debug("Cookies disponibles:");
                for (Cookie cookie : request.getCookies()) {
                    log.debug("  {}: {}", cookie.getName(), cookie.getValue());
                }
            } else {
                log.debug("No hay cookies en la solicitud");
            }
        }

        // // Verificar el estado del SecurityContext antes de continuar
        // var auth = SecurityContextHolder.getContext().getAuthentication();
        // if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
        //     log.info("✓ SecurityContext configurado con usuario: {} y authorities: {}", 
        //         auth.getPrincipal(), auth.getAuthorities());
        // } else {
        //     log.warn("✗ SecurityContext NO configurado o usuario anónimo");
        // }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // log.debug("Extrayendo token de la solicitud...");
        
        // Primero buscar en header Authorization
        String authHeader = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);
        // log.debug("Header Authorization: {}", authHeader);
        
        if (authHeader != null && authHeader.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            log.info("✓ Token encontrado en header Authorization");
            String token = authHeader.substring(TokenJwtConfig.PREFIX_TOKEN.length());
            log.debug("Token extraído del header (primeros 50 chars): {}", 
                token.substring(0, Math.min(50, token.length())) + "...");
            return token;
        }

        // Si no está en header, buscar en cookies
        log.debug("Buscando token en cookies...");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                String cookieValue = cookie.getValue().length() > 50 ? 
                    cookie.getValue().substring(0, 50) + "..." : cookie.getValue();
                log.debug("Cookie encontrada: {} = {}", cookie.getName(), cookieValue);
                    
                if ("token".equals(cookie.getName())) {
                    log.info("✓ Token encontrado en cookie 'token'");
                    return cookie.getValue();
                }
            }
            log.warn("✗ Cookie 'token' no encontrada");
        } else {
            log.warn("✗ No hay cookies en la solicitud");
        }

        log.warn("✗ No se encontró token en header ni cookies");
        return null;
    }
}
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
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@WebFilter("/*")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);

        // Si no está en header, buscar en cookie
        if (token == null) {
            logger.warn("No se encontró token en el header, buscando en cookies...");
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("token".equals(cookie.getName())) { // Cambia "JWT" por el nombre de tu cookie
                        token = cookie.getValue();
                        break;
                    }
                }
            }else{
                logger.warn("No se encontraron cookies en la solicitud");
            }
        } else if (token.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            token = token.substring(TokenJwtConfig.PREFIX_TOKEN.length());
        }

        if (token != null && jwtService.esTokenValido(token)) {
            String email = jwtService.obtenerEmailDelToken(token);
            Claims claims = jwtService.obtenerClaims(token);

            @SuppressWarnings("unchecked")
            List<String> roles = claims.get("roles", List.class);

            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}

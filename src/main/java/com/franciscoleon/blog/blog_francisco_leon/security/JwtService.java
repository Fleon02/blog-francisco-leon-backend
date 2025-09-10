package com.franciscoleon.blog.blog_francisco_leon.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private static final long EXPIRATION_TIME = 86400000L; // 1 día en milisegundos
    //private static final long EXPIRATION_TIME = 120000L; // 2 minutos en milisegundos

    @Autowired
    private TokenJwtConfig tokenJwtConfig;

    public String generarToken(User usuario) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = List.of(usuario.getRole().name());
        claims.put("roles", roles);
        claims.put("username", usuario.getUsername());

        log.debug("Generando token JWT para usuario: {}", usuario.getEmail());

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(tokenJwtConfig.getSecretKey())
                .compact();
    }

    public String obtenerEmailDelToken(String token) {
        return obtenerClaims(token).getSubject();
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(tokenJwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean esTokenValido(String token) {
        try {
            Claims claims = obtenerClaims(token);
            boolean valido = claims.getExpiration().after(new Date());
            return valido;
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            return false;
        } catch (SignatureException | MalformedJwtException e) {
            log.error("Token inválido: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            return false;
        }
    }
}

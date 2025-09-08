package com.franciscoleon.blog.blog_francisco_leon.security;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import com.franciscoleon.blog.blog_francisco_leon.config.AppConfig;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TokenJwtConfig {

    private static final Logger log = LoggerFactory.getLogger(TokenJwtConfig.class);

    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";

    private final SecretKey secretKey;

    public TokenJwtConfig(AppConfig appConfig) {
        byte[] keyBytes = Base64.getDecoder().decode(appConfig.getJwt().getSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        log.debug("TokenJwtConfig inicializado correctamente"); // 👈 no imprime secretos
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}

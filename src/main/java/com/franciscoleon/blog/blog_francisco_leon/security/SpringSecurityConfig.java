package com.franciscoleon.blog.blog_francisco_leon.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.franciscoleon.blog.blog_francisco_leon.config.AppConfig;
import com.franciscoleon.blog.blog_francisco_leon.security.filter.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import java.util.Arrays;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AppConfig appConfig; // ✅ Inyectar correctamente

    @Autowired
    private Environment env; // Para leer el profile activo

    // Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de seguridad principal
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilita CORS
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para APIs
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/test/**").hasAnyAuthority("ADMIN") // Probar autentificacion con Test
                        .requestMatchers("/api/auth/**").permitAll() // Permite todos los endpoints de autenticación
                        .requestMatchers("/api/auth-cookie/**").permitAll() // Permite todos los endpoints de autenticación con cookies
                        .anyRequest().authenticated() // Otros requieren autenticación
                );

        return http.build();
    }

    // Configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        String frontendUrl = appConfig.getFrontend().getUrl();

        if (frontendUrl == null || frontendUrl.isEmpty()) {
            frontendUrl = "http://localhost:4321"; // fallback
        }

        // Leer profile activo
        String activeProfile = Arrays.stream(env.getActiveProfiles()).findFirst().orElse("dev");

        if (activeProfile.equals("dev")) {
            // Modo desarrollo: permitir cualquier origen
            configuration.setAllowedOriginPatterns(Arrays.asList("*"));
            System.out.println("[CORS CONFIG] DEV: Se permiten todos los orígenes");
        } else {
            // Producción: solo frontendUrl
            configuration.setAllowedOriginPatterns(Arrays.asList(frontendUrl));
            System.out.println("[CORS CONFIG] PROD: Se permite solo " + frontendUrl);
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // Registrar el filtro CORS con alta prioridad
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(
                new CorsFilter(corsConfigurationSource()));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
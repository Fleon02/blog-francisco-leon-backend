package com.franciscoleon.blog.blog_francisco_leon.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
                        .anyRequest().authenticated() // Otros requieren autenticación
                );

        return http.build();
    }

    // Configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ Usar la instancia inyectada, no crear una nueva
        String frontendUrl = appConfig.getFrontend().getUrl();

        // Si la variable de entorno no está definida, usar el valor por defecto
        if (frontendUrl == null || frontendUrl.isEmpty()) {
            frontendUrl = "http://localhost:4321"; // fallback en desarrollo
            System.out.println("[CORS CONFIG] FRONTEND_URL (fallback): " + frontendUrl);
        }

        // Mostrar en consola qué URL está usando
        System.out.println("[CORS CONFIG] FRONTEND_URL: " + frontendUrl);
        System.out.println("SECRET KEY: " + appConfig.getJwt().getSecret()); // Para depuración

        configuration.setAllowedOriginPatterns(Arrays.asList(frontendUrl)); // Permitir solo la URL del frontend
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
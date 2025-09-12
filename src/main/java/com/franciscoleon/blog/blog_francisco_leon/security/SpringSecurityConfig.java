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
    private AppConfig appConfig;

    @Autowired
    private Environment env;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/test/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/auth-cookie/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        String activeProfile = Arrays.stream(env.getActiveProfiles()).findFirst().orElse("dev");
        
        if (activeProfile.equals("dev")) {
            // Desarrollo: permitir orígenes locales específicos
            configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "http://127.0.0.1:*", 
                "http://192.168.1.*:*",
                "http://10.0.2.*:*" // Para emuladores Android
            ));
            System.out.println("[CORS CONFIG] DEV: Permitiendo orígenes locales");
        } else {
            // Producción: solo frontendUrl específico
            String frontendUrl = appConfig.getFrontend().getUrl();
            if (frontendUrl == null || frontendUrl.isEmpty()) {
                frontendUrl = "https://your-frontend-domain.com"; // Cambiar por tu dominio real
            }
            configuration.setAllowedOriginPatterns(Arrays.asList(frontendUrl));
            System.out.println("[CORS CONFIG] PROD: Permitiendo solo " + frontendUrl);
        }

        // Métodos permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Headers permitidos - IMPORTANTE para cookies
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Headers expuestos
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
        
        // CRÍTICO: permitir credenciales (cookies)
        configuration.setAllowCredentials(true);
        
        // Configurar max age para preflight
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(
                new CorsFilter(corsConfigurationSource()));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
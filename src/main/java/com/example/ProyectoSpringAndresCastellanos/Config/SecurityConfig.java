package com.example.ProyectoSpringAndresCastellanos.Config;

import com.example.ProyectoSpringAndresCastellanos.Security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // ---------------------------------------------------
    // Cadena principal de seguridad
    // ---------------------------------------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Habilito CORS con la configuración de abajo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // API REST stateless: CSRF no aplica
                .csrf(csrf->csrf.disable())
                // Sin sesiones: cada request se autentica con su propio token
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Login y registro, públicos (si estuvieran protegidos,
                        // nadie podría obtener un token para empezar)
                        .requestMatchers("/auth/**").permitAll()
                        // Swagger accesible sin token
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        // Peticiones OPTIONS libres: el navegador las manda antes
                        // de cada request real al hacer CORS (preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Todo lo demás (/bodegas, /productos, /movimientos...) requiere token
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    // ---------------------------------------------------
    // CORS: permite que tu frontend HTML/CSS/JS (día 6) consuma la API
    // ---------------------------------------------------
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        // Ajusta el puerto/origen según dónde sirvas tu frontend
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    // ---------------------------------------------------
    // Encoder de contraseñas (BCrypt)
    // ---------------------------------------------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ---------------------------------------------------
    // Conecta UserDetailsService + PasswordEncoder
    // ---------------------------------------------------
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    // ---------------------------------------------------
    // AuthenticationManager: lo usará AuthController para el login
    // ---------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
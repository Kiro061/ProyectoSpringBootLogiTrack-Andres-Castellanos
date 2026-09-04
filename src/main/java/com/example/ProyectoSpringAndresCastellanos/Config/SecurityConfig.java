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

/**
 * Configuración principal de seguridad de la aplicación.
 *
 * Define las reglas de acceso a los endpoints, la autenticación
 * mediante JWT, la configuración CORS y el cifrado de contraseñas.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro encargado de validar los tokens JWT.
    private final JwtAuthFilter jwtAuthFilter;

    // Servicio utilizado para buscar los usuarios durante el login.
    private final UserDetailsService userDetailsService;

    /**
     * Configura la cadena principal de seguridad de la aplicación.
     *
     * Define qué endpoints son públicos y cuáles requieren
     * autenticación mediante un token JWT.
     *
     * @param http configuración de seguridad HTTP
     * @return cadena de filtros de seguridad
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Permite las peticiones CORS desde el frontend.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Desactiva CSRF porque la aplicación utiliza una API REST.
                .csrf(csrf -> csrf.disable())

                // La aplicación no utiliza sesiones; cada petición utiliza su JWT.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Login y registro no requieren autenticación.
                        .requestMatchers("/auth/**").permitAll()

                        // Permite acceder a Swagger sin utilizar un token.
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Permite las peticiones OPTIONS utilizadas por CORS.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Todos los demás endpoints requieren autenticación.
                        .anyRequest().authenticated()
                )

                // Ejecuta el filtro JWT antes del filtro de autenticación estándar.
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    /**
     * Configura CORS para permitir que el frontend se comunique
     * con el backend.
     *
     * @return configuración de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Direcciones desde las que se permite acceder a la API.
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));

        // Métodos HTTP permitidos.
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        // Permite cualquier encabezado HTTP.
        config.setAllowedHeaders(List.of("*"));

        // Permite el envío de credenciales.
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // Aplica la configuración a todas las rutas.
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Configura el codificador utilizado para proteger las contraseñas.
     *
     * BCrypt transforma las contraseñas en valores cifrados antes
     * de almacenarlas en la base de datos.
     *
     * @return codificador BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el proveedor de autenticación.
     *
     * Utiliza UserDetailsService para buscar al usuario y
     * PasswordEncoder para comprobar su contraseña.
     *
     * @return proveedor de autenticación configurado
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * Obtiene el administrador de autenticación utilizado
     * durante el proceso de login.
     *
     * @param config configuración de autenticación de Spring
     * @return administrador de autenticación
     * @throws Exception si ocurre un error al obtenerlo
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
}

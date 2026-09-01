package com.example.ProyectoSpringAndresCastellanos.Controller;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.LoginRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Request.RegisterRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.AuthResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Model.Usuario;
import com.example.ProyectoSpringAndresCastellanos.Repository.UsuarioRepository;
import com.example.ProyectoSpringAndresCastellanos.Security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro e inicio de sesión")
@SecurityRequirements
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "No requiere token"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos no válidos / Body mal estructurado"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessRuleException("El username ya esta en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombreCompleto());
        usuario.setRol(request.getRol());
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        String token = jwtService.generarToken(usuario);

        return ResponseEntity.ok(
                new AuthResponse(token, usuario.getUsername(), usuario.getRol().name())
        );
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "No requiere token"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
                    @ApiResponse(responseCode = "400", description = "Datos no válidos / Credenciales incorrectas"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BusinessRuleException("Credenciales invalidas");
        }

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessRuleException("Credenciales invalidas"));

        String token = jwtService.generarToken(usuario);

        return ResponseEntity.ok(
                new AuthResponse(token, usuario.getUsername(), usuario.getRol().name())
        );
    }
}
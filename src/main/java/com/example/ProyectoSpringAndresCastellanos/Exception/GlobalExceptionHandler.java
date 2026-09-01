package com.example.ProyectoSpringAndresCastellanos.Exception;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 - Recurso no encontrado (entidad JPA o tu excepción propia)
    @ExceptionHandler({EntityNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage(), "RESOURCE_NOT_FOUND");
    }

    // 400 - Reglas de negocio violadas
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), "BUSINESS_RULE_VIOLATION");
    }

    // 400 - Errores de validación (@NotBlank, @Min, @Size, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errorCode", "VALIDATION_FAILED");

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 400 - El JSON del body viene mal formado o no se pudo parsear
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleParsingError(HttpMessageNotReadableException ex) {
        return construirRespuesta(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es válido", "BAD_REQUEST_BODY");
    }

    // 401 - Credenciales inválidas / autenticación fallida
    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthError(Exception ex) {
        return construirRespuesta(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", "UNAUTHORIZED");
    }

    // 403 - Usuario autenticado, pero sin el rol/permiso requerido (@PreAuthorize)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return construirRespuesta(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta acción", "ACCESS_DENIED");
    }

    // 500 - Cualquier otro error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Error no controlado: ", ex);
        return construirRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", "INTERNAL_SERVER_ERROR");
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(HttpStatus status, String mensaje, String errorCode) {
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), status.value(), mensaje, errorCode);
        return ResponseEntity.status(status).body(error);
    }
}
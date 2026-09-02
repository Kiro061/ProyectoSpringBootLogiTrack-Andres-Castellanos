# LogiTrack S.A. — Documento Técnico del Sistema

Sistema backend de gestión y auditoría de bodegas, desarrollado en Spring Boot 4 con
autenticación JWT, control de roles, auditoría automática de cambios y reportes
calculados a partir del historial de movimientos.

---

## 1. Diagrama de clases

```mermaid
classDiagram
    class Bodega {
        +Long id
        +String nombre
        +String ubicacion
        +Integer capacidad
        +String encargado
    }

    class Producto {
        +Long id
        +String nombre
        +String categoria
        +Integer stock
        +BigDecimal precio
    }

    class Movimiento {
        +Long id
        +LocalDateTime fecha
        +TipoMovimiento tipoMovimiento
        +List~MovimientoDetalle~ detalles
    }

    class MovimientoDetalle {
        +Long id
        +int cantidad
    }

    class Usuario {
        +Long id
        +String username
        +String password
        +String nombre
        +Rol rol
        +Boolean activo
        +LocalDateTime fechaCreacion
    }

    class Auditoria {
        +Long id
        +TipoOperacion tipoOperacion
        +LocalDateTime fechaHora
        +String entidadAfectada
        +Long entidadId
        +String valoresAnteriores
        +String valoresNuevos
    }

    class Rol {
        <<enumeration>>
        ADMIN
        EMPLEADO
    }

    class TipoMovimiento {
        <<enumeration>>
        ENTRADA
        SALIDA
        TRANSFERENCIA
    }

    class TipoOperacion {
        <<enumeration>>
        INSERT
        UPDATE
        DELETE
    }

    Movimiento "1" --> "1" Usuario : usuario responsable
    Movimiento "0..1" --> "1" Bodega : bodegaOrigen
    Movimiento "0..1" --> "1" Bodega : bodegaDestino
    Movimiento "1" --> "*" MovimientoDetalle : detalles
    MovimientoDetalle "*" --> "1" Producto : producto
    Auditoria "0..1" --> "1" Usuario : usuario responsable
    Usuario --> Rol
    Movimiento --> TipoMovimiento
    Auditoria --> TipoOperacion
```

**Notas sobre el modelo:**

- `Bodega`, `Producto` y `Movimiento` implementan la interfaz `Auditable`
  (`getId()` + `getAuditData()`), que es lo que sus respectivos `Service`
  usan para construir el registro de auditoría antes/después de cada
  operación.
- `Movimiento.bodegaOrigen` y `bodegaDestino` son opcionales a nivel de
  columna porque su obligatoriedad depende del `tipoMovimiento`
  (ENTRADA solo exige destino, SALIDA solo exige origen, TRANSFERENCIA
  exige ambos). Esa regla se valida en `MovimientoService`, no en la
  base de datos.
- El **stock por bodega no existe como columna**: `Producto.stock` es
  un total global. El stock "por bodega" que expone
  `GET /reportes/resumen` se calcula en tiempo de consulta, sumando
  entradas y restando salidas de la tabla `movimiento_detalle`.

---

## 2. Descripción de la arquitectura

El proyecto sigue una arquitectura en capas típica de Spring Boot:

```
Cliente (Frontend HTML/CSS/JS)
        │  HTTP + JSON (Authorization: Bearer <token>)
        ▼
┌───────────────────────────────────────────────────────────┐
│                     Filtro de seguridad                    │
│  JwtAuthFilter  →  valida el token en cada request          │
└───────────────────────────────────────────────────────────┘
        ▼
┌─────────────┐     ┌─────────────┐     ┌──────────────┐     ┌───────┐
│  Controller  │ ──▶ │   Service   │ ──▶ │  Repository  │ ──▶ │ MySQL │
│ (REST + DTO) │     │ (reglas de  │     │ (Spring Data │     │       │
│              │     │  negocio)   │     │     JPA)     │     │       │
└─────────────┘     └─────────────┘     └──────────────┘     └───────┘
        ▲                    │
        │                    ▼
   DTO Request/Response   Auditoria (registro automático
   (Mapper convierte      INSERT/UPDATE/DELETE)
    Entity ↔ DTO)
```

### 2.1. Paquetes principales

| Paquete | Responsabilidad |
|---|---|
| `Controller` | Expone los endpoints REST, valida el body con `@Valid` y delega en el `Service`. No contiene lógica de negocio. |
| `Service` | Reglas de negocio: valida tipos de movimiento, calcula stock, arma los `valorAnterior`/`valorNuevo` de cada auditoría, etc. |
| `Repository` | Interfaces `JpaRepository`, con métodos derivados (`findByStockLessThan`) y consultas `@Query` para casos más complejos (reportes). |
| `Model` | Entidades JPA (`@Entity`) que mapean 1:1 con las tablas de MySQL. |
| `Dto.Request` / `Dto.Response` | Objetos de entrada/salida de la API. Nunca se expone una entidad directamente, para no filtrar campos internos (ej. `password`) ni acoplar la API a la estructura de la base de datos. |
| `Mapper` | Convierte entre `Entity` y `Dto` (a mano, sin librerías como MapStruct). |
| `Config` | `SecurityConfig` (reglas de acceso HTTP), `OpenApiConfig` (Swagger). |
| `Security` | `JwtService` (genera/valida tokens), `JwtAuthFilter` (intercepta cada request), `UserDetailsServiceImpl` (carga el usuario para Spring Security). |
| `Exception` | `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza el manejo de errores y siempre responde con el mismo formato JSON (`ErrorResponse`). |

### 2.2. Seguridad (Spring Security + JWT)

1. El cliente llama a `POST /auth/login` con `username`/`password`.
2. `AuthController` delega en el `AuthenticationManager` de Spring Security, que compara la contraseña contra el hash BCrypt guardado en `usuarios.password`.
3. Si es válida, `JwtService.generarToken()` firma un JWT con el `username` como *subject* y el `rol` como *claim* extra, con una expiración de 30 minutos (`jwt.expiration=1800000` ms).
4. El cliente guarda ese token y lo manda en cada request protegido: `Authorization: Bearer <token>`.
5. `JwtAuthFilter` (un `OncePerRequestFilter`) intercepta **todas** las requests, lee el header, valida la firma y la expiración, y si es válido carga la autenticación en el `SecurityContextHolder` — así el resto del pipeline de Spring Security (y los `@PreAuthorize` de los controllers) ya saben quién es el usuario y qué rol tiene, sin volver a tocar la base de datos en cada request salvo para cargar el `UserDetails`.
6. `SecurityConfig` define qué rutas son públicas (`/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`) y cuáles requieren autenticación. Los endpoints de creación/edición/eliminación además llevan `@PreAuthorize("hasRole('ADMIN')")` a nivel de método (activado con `@EnableMethodSecurity`).

### 2.3. Auditoría automática

Cada `Service` de una entidad auditable (`Bodega`, `Producto`, `Movimiento`) captura el estado **antes** de modificar (usando `Auditable.getAuditData()`), ejecuta la operación, y luego llama a `AuditoriaService.registrar(...)` con el tipo de operación (`INSERT`/`UPDATE`/`DELETE`), el usuario responsable (sacado del `SecurityContextHolder`, no del body) y los valores antes/después. Esto queda persistido en la tabla `auditoria` y expuesto solo a `ADMIN` vía `/auditorias`.

### 2.4. Manejo de errores

`GlobalExceptionHandler` centraliza todas las excepciones y siempre responde con la misma forma:

```json
{
  "timestamp": "2026-08-31T20:23:27.39",
  "status": 403,
  "mensaje": "No tienes permisos para realizar esta acción",
  "errorCode": "ACCESS_DENIED"
}
```

| Excepción | HTTP | errorCode |
|---|---|---|
| `EntityNotFoundException` / `ResourceNotFoundException` | 404 | `RESOURCE_NOT_FOUND` |
| `BusinessRuleException` | 400 | `BUSINESS_RULE_VIOLATION` |
| `MethodArgumentNotValidException` (`@Valid` falló) | 400 | `VALIDATION_FAILED` |
| `HttpMessageNotReadableException` (JSON mal formado) | 400 | `BAD_REQUEST_BODY` |
| `BadCredentialsException` / `AuthenticationException` | 401 | `UNAUTHORIZED` |
| `AccessDeniedException` (`@PreAuthorize` rechazó) | 403 | `ACCESS_DENIED` |
| Cualquier otra excepción | 500 | `INTERNAL_SERVER_ERROR` |

### 2.5. Documentación (Swagger/OpenAPI 3)

`springdoc-openapi-starter-webmvc-ui` genera la especificación OpenAPI automáticamente a partir de los controllers, anotados con `@ApiResponses`. `OpenApiConfig` agrega el esquema de seguridad `bearerAuth`, así Swagger UI (`/swagger-ui.html`) muestra el botón **Authorize** para pegar el token y probar los endpoints protegidos directamente ahí.

Cada endpoint se documenta con este mismo patrón, por ejemplo en `AuditoriaController`:

```java
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "Auditoría listada exitosamente"),
                @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
)
```

### 2.6. Frontend

Un frontend estático (HTML + CSS + JS puro, sin frameworks) consume la API vía `fetch`. Guarda el token en `localStorage` tras el login y lo reenvía en cada petición. El backend habilita CORS solo para `http://127.0.0.1:5500` y `http://localhost:5500` (el puerto de Live Server), evitando así llamadas desde orígenes no autorizados.

---

## 3. JWT: ejemplo y uso

### 3.1. Cómo se genera

`JwtService.generarToken()` firma el token con el algoritmo **HS256**, usando una clave secreta simétrica (`jwt.secret`, configurada en `application.properties`). El payload incluye:

- `sub` (*subject*): el `username` del usuario.
- `rol`: claim personalizado con el rol (`ADMIN` o `EMPLEADO`).
- `iat` (*issued at*): fecha de emisión.
- `exp` (*expiration*): fecha de expiración, 30 minutos después (`jwt.expiration=1800000` ms).

### 3.2. Ejemplo real (usuario `admin1`)

Token obtenido al llamar `POST /auth/login`:

```
eyJhbGciOiJIUzI1NiJ9.eyJyb2wiOiJBRE1JTiIsInN1YiI6ImFkbWluMSIsImlhdCI6MTc4ODIxMjAxOCwiZXhwIjoxNzg4MjEzODE4fQ.P7o7-Vz68CRy7XLzh7MJ8HnjyiLbaD4VeJam3tlV9vA
```

Un JWT tiene 3 partes separadas por `.` → `header.payload.signature`:

**Header** (algoritmo usado):
```json
{
  "alg": "HS256"
}
```

**Payload** (los datos del usuario, decodificado de la parte del medio):
```json
{
  "rol": "ADMIN",
  "sub": "admin1",
  "iat": 1788212018,
  "exp": 1788213818
}
```

**Signature**: el hash HMAC-SHA256 del header+payload firmado con `jwt.secret`. Es lo que impide que alguien modifique el payload (por ejemplo, cambiar `"rol":"EMPLEADO"` a `"rol":"ADMIN"`) sin invalidar el token — cualquier cambio en header o payload rompe la firma, y `JwtService.esTokenValido()` lo rechaza.

> El payload de un JWT **no está encriptado, solo codificado en Base64** —
> cualquiera puede decodificarlo y leerlo (ej. en jwt.io). Por eso nunca se
> deben meter datos sensibles (contraseñas, etc.) dentro del token; solo
> identificadores como `username` y `rol`.

### 3.3. Cómo se usa

En cualquier endpoint protegido, el token va en el header HTTP `Authorization`, con el prefijo `Bearer `:

```http
GET /bodegas HTTP/1.1
Host: localhost:8080
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2wiOiJBRE1JTiIsInN1YiI6ImFkbWluMSIsImlhdCI6MTc4ODIxMjAxOCwiZXhwIjoxNzg4MjEzODE4fQ.P7o7-Vz68CRy7XLzh7MJ8HnjyiLbaD4VeJam3tlV9vA
```

- En **Postman**: pestaña *Authorization* → tipo *Bearer Token* → pegar solo el token (sin escribir "Bearer").
- En **Swagger UI**: botón *Authorize* 🔒 arriba a la derecha → pegar solo el token (Swagger agrega el prefijo `Bearer` solo).
- En el **frontend** (`js/api.js`): se agrega automáticamente el header en cada `fetch` leyendo el token guardado en `localStorage`.

Si el token falta, está mal formado, expiró, o la firma no coincide, `JwtAuthFilter` simplemente no autentica al usuario y la request sigue como anónima — lo que hace que `SecurityConfig` la rechace con `401 Unauthorized` al no cumplir la regla de "autenticado" del endpoint. Si el token es válido pero el rol no alcanza (ej. un `EMPLEADO` intentando `POST /bodegas`), el rechazo lo hace `@PreAuthorize` con `403 Forbidden`.

# LogiTrack S.A. — Sistema de Gestión de Inventario

## 📋 Descripción del proyecto

**LogiTrack S.A.** es una aplicación web desarrollada para gestionar el inventario y las operaciones de bodegas de una empresa logística.

El proyecto permite administrar productos, bodegas y movimientos de inventario, además de implementar autenticación mediante **JWT** y un sistema de **auditoría** para registrar las operaciones realizadas.

El backend fue desarrollado utilizando **Spring Boot** y expone una API REST que puede ser consumida mediante Swagger, Postman o un frontend web desarrollado con HTML, CSS y JavaScript.

### 🎯 Objetivos

- Gestionar las bodegas de la empresa.
- Gestionar productos e inventario.
- Registrar movimientos de entrada, salida y transferencia.
- Consultar movimientos realizados.
- Implementar autenticación y autorización mediante JWT.
- Controlar el acceso dependiendo del rol del usuario.
- Registrar operaciones importantes mediante auditoría.
- Exponer los servicios mediante una API REST.
- Proporcionar una interfaz frontend básica para consumir los endpoints.

---

# 🛠️ Tecnologías utilizadas

### Backend

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- MySQL
- Maven
- Lombok
- Jakarta Validation
- Swagger / OpenAPI

### Frontend

- HTML5
- CSS3
- JavaScript
- Fetch API

### Herramientas

- IntelliJ IDEA
- MySQL / MySQL Workbench
- Postman
- Swagger UI
- Git
- GitHub

---

# 📁 Estructura del proyecto

```text
ProyectoSpringAndresCastellanos/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com.example.ProyectoSpringAndresCastellanos/
│       │       ├── Config/
│       │       ├── Controller/
│       │       ├── Dto/
│       │       │   ├── Request/
│       │       │   └── Response/
│       │       ├── Entity/
│       │       ├── Exception/
│       │       ├── Mapper/
│       │       ├── Repository/
│       │       ├── Security/
│       │       └── Service/
│       │
│       └── resources/
│           └── application.properties
│
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── styles.css
│   └── js/
│       └── app.js
│
├── logitrack_db.sql
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

# 🗄️ Base de datos

El proyecto utiliza **MySQL** como sistema gestor de base de datos.

La base de datos utilizada es:

```text
logitrack_db
```

### Principales tablas

```text
usuarios
    │
    ├── movimientos
    │       │
    │       └── movimiento_detalle
    │
    └── auditoria

bodegas
    │
    └── movimientos

productos
    │
    └── movimiento_detalle
```

Las principales entidades son:

- `usuarios`
- `bodegas`
- `productos`
- `movimientos`
- `movimiento_detalle`
- `auditoria`

---

# ⚙️ Instalación

## 1. Clonar el repositorio

```bash
git clone https://github.com/Kiro061/ProyectoSpringBootLogiTrack-Andres-Castellanos.git
```

Ingresar a la carpeta:

```bash
cd ProyectoSpringBootLogiTrack-Andres-Castellanos
```

---

## 2. Crear la base de datos

El proyecto incluye el archivo:

```text
logitrack_db.sql
```

Este archivo contiene la estructura de la base de datos y registros básicos para realizar pruebas.

Desde MySQL se puede ejecutar:

```sql
SOURCE ruta/al/proyecto/logitrack_db.sql;
```

También se puede abrir el archivo desde **MySQL Workbench** y ejecutar el script completo.

Después de ejecutarlo debe existir:

```text
logitrack_db
```

---

# 🔐 Configuración de conexión

Abrir:

```text
src/main/resources/application.properties
```

Configurar las credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/logitrack_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

También debe estar configurada la clave utilizada para JWT:

```properties
jwt.secret=TU_CLAVE_SECRETA
jwt.expiration=86400000
```

> No se recomienda subir contraseñas reales ni claves JWT privadas al repositorio público.

---

# ▶️ Ejecución del backend

El proyecto utiliza Maven.

### Windows

```bash
mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

También puede ejecutarse directamente desde IntelliJ IDEA utilizando la clase principal de Spring Boot.

Si la aplicación inicia correctamente, estará disponible normalmente en:

```text
http://localhost:8080
```

---

# 📚 Swagger

La documentación de la API está disponible mediante Swagger UI.

Abrir en el navegador:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger permite consultar los endpoints disponibles y realizar pruebas directamente desde el navegador.

---

# 🔑 Autenticación

La API utiliza **JWT (JSON Web Token)** para proteger los endpoints.

Primero se debe realizar el inicio de sesión.

### Login

```http
POST /auth/login
```

Body:

```json
{
    "username": "admin1",
    "password": "123456"
}
```

La respuesta contiene un token JWT.

Ejemplo:

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

El token debe utilizarse para acceder a los endpoints protegidos.

En Swagger se puede utilizar el botón:

```text
Authorize
```

e ingresar:

```text
Bearer TU_TOKEN
```

---

# 🔗 Principales endpoints

## 🔐 Autenticación

### Registrar usuario

```http
POST /auth/register
```

### Iniciar sesión

```http
POST /auth/login
```

Ejemplo:

```json
{
    "username": "admin1",
    "password": "123456"
}
```

---

# 🏢 Bodegas

### Crear bodega

```http
POST /bodegas
```

Ejemplo:

```json
{
    "nombre": "Bodega Principal",
    "ubicacion": "Bucaramanga",
    "capacidad": 1000,
    "encargado": "Carlos Ramirez"
}
```

### Listar bodegas

```http
GET /bodegas
```

### Consultar bodega

```http
GET /bodegas/{id}
```

Ejemplo:

```http
GET /bodegas/1
```

### Actualizar bodega

```http
PUT /bodegas/{id}
```

### Eliminar bodega

```http
DELETE /bodegas/{id}
```

---

# 📦 Productos

### Crear producto

```http
POST /productos
```

Ejemplo:

```json
{
    "nombre": "Laptop Lenovo",
    "categoria": "Tecnologia",
    "stock": 20,
    "precio": 1800000
}
```

### Listar productos

```http
GET /productos
```

### Consultar producto

```http
GET /productos/{id}
```

### Consultar productos con stock bajo

```http
GET /productos/stock-bajo
```

### Actualizar producto

```http
PUT /productos/{id}
```

### Eliminar producto

```http
DELETE /productos/{id}
```

---

# 🔄 Movimientos de inventario

El sistema permite registrar tres tipos de movimientos:

```text
ENTRADA
SALIDA
TRANSFERENCIA
```

### Registrar movimiento

```http
POST /movimientos
```

Ejemplo:

```json
{
    "tipoMovimiento": "ENTRADA",
    "bodegaDestinoId": 1,
    "detalles": [
        {
            "productoId": 1,
            "cantidad": 10
        }
    ]
}
```

### Listar movimientos

```http
GET /movimientos
```

### Consultar movimiento

```http
GET /movimientos/{id}
```

### Consultar movimientos por rango de fechas

```http
GET /movimientos/fecha
```

---

# 📊 Reportes

El proyecto cuenta con un endpoint para consultar un resumen general de los movimientos:

```http
GET /reportes/resumen
```

Este endpoint permite obtener información relacionada con las operaciones de inventario registradas.

---

# 📝 Auditoría

El sistema registra las operaciones realizadas sobre las principales entidades.

Endpoint:

```http
GET /auditoria
```

El acceso a la auditoría está restringido a usuarios con rol:

```text
ADMIN
```

La auditoría permite mantener trazabilidad sobre las operaciones realizadas en el sistema.

---

# 👥 Roles

El sistema maneja dos roles principales:

| Rol | Descripción |
|---|---|
| `ADMIN` | Tiene acceso a las operaciones administrativas y auditoría |
| `EMPLEADO` | Puede realizar las operaciones permitidas para usuarios operativos |

La autorización se realiza mediante **Spring Security + JWT**.

---

# 🖥️ Frontend

El proyecto incluye una carpeta:

```text
frontend/
```

Esta contiene un frontend básico desarrollado utilizando HTML, CSS y JavaScript.

```text
frontend/
├── index.html
├── css/
│   └── styles.css
└── js/
    └── app.js
```

El frontend consume directamente los endpoints REST del backend mediante `fetch()`.

### Funcionalidades principales

- Inicio de sesión.
- Almacenamiento del token JWT.
- Autenticación de solicitudes.
- Consulta de bodegas.
- Consulta de productos.
- Consulta de stock bajo.
- Consulta de movimientos.
- Interacción con la API REST.

### Ejecución

Primero iniciar el backend Spring Boot:

```bash
mvnw.cmd spring-boot:run
```

Después abrir:

```text
frontend/index.html
```

También se puede utilizar una extensión como **Live Server** desde Visual Studio Code para ejecutar el frontend.

---

# 🔌 Comunicación Frontend → Backend

El frontend realiza solicitudes HTTP al backend.

Ejemplo de login:

```javascript
fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify({
        username: "admin1",
        password: "123456"
    })
});
```

Para endpoints protegidos se envía el JWT:

```javascript
fetch("http://localhost:8080/bodegas", {
    method: "GET",
    headers: {
        "Authorization": "Bearer " + token
    }
});
```

---

# 🧪 Pruebas con Postman

Los endpoints pueden probarse utilizando Postman.

### Flujo recomendado

1. Ejecutar el backend.
2. Realizar `POST /auth/login`.
3. Copiar el JWT recibido.
4. Agregar el token en Authorization.
5. Seleccionar:
   ```text
   Bearer Token
   ```
6. Probar los endpoints de bodegas.
7. Probar los endpoints de productos.
8. Registrar movimientos.
9. Consultar movimientos.
10. Consultar auditoría utilizando un usuario `ADMIN`.

---

# 📸 Capturas de pantalla

Las siguientes capturas deben incluirse en el repositorio para evidenciar el funcionamiento del proyecto.

## Swagger

Captura de la documentación general:

```text
docs/
└── swagger-general.png
```

![Swagger - Documentación general](docs/swagger-general.png)

---

## Login

Captura de la prueba del endpoint:

```text
POST /auth/login
```

Archivo sugerido:

```text
docs/
└── login-swagger.png
```

![Login](docs/login-swagger.png)

---

## Bodegas

Captura de consulta o creación de una bodega:

```text
GET /bodegas
POST /bodegas
```

Archivo sugerido:

```text
docs/
└── bodegas.png
```

![Pruebas de bodegas](docs/bodegas.png)

---

## Productos

Captura de las pruebas realizadas sobre productos:

```text
GET /productos
GET /productos/stock-bajo
POST /productos
```

Archivo sugerido:

```text
docs/
└── productos.png
```

![Pruebas de productos](docs/productos.png)

---

## Movimientos

Captura del registro y consulta de movimientos:

```text
POST /movimientos
GET /movimientos
```

Archivo sugerido:

```text
docs/
└── movimientos.png
```

![Pruebas de movimientos](docs/movimientos.png)

---

## Postman

Captura de las pruebas realizadas mediante Postman.

Archivo sugerido:

```text
docs/
└── postman-pruebas.png
```

![Pruebas Postman](docs/postman-pruebas.png)

> Las imágenes anteriores son rutas de ejemplo. Deben reemplazarse por las capturas reales tomadas durante las pruebas del proyecto.

---

# 📂 Estructura recomendada para las evidencias

Se recomienda crear una carpeta:

```text
docs/
├── swagger-general.png
├── login-swagger.png
├── bodegas.png
├── productos.png
├── movimientos.png
└── postman-pruebas.png
```

De esta manera, las capturas quedan organizadas y pueden ser visualizadas directamente desde el README de GitHub.

---

# 🔒 Seguridad

El proyecto implementa:

- Autenticación mediante JWT.
- Autorización basada en roles.
- Contraseñas almacenadas utilizando BCrypt.
- Protección de endpoints mediante Spring Security.
- Validación de datos mediante Jakarta Validation.
- Auditoría de operaciones.

Para ambientes reales se recomienda utilizar variables de entorno para almacenar:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

---

# 🚀 Flujo general de funcionamiento

```text
                 ┌──────────────────┐
                 │     FRONTEND     │
                 │   HTML/CSS/JS    │
                 └────────┬─────────┘
                          │
                          │ HTTP / JSON
                          ▼
                 ┌──────────────────┐
                 │   SPRING BOOT    │
                 │    REST API      │
                 └────────┬─────────┘
                          │
             ┌────────────┼────────────┐
             │            │            │
             ▼            ▼            ▼
        ┌─────────┐  ┌──────────┐  ┌──────────┐
        │  JWT    │  │ SERVICES │  │ SECURITY │
        └─────────┘  └────┬─────┘  └──────────┘
                           │
                           ▼
                    ┌─────────────┐
                    │    JPA      │
                    │  Hibernate  │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
                    │    MySQL    │
                    │ logitrack_db│
                    └─────────────┘
```

---

# ✅ Funcionalidades implementadas

- [x] Registro de usuarios.
- [x] Login mediante JWT.
- [x] Autorización por roles.
- [x] CRUD de bodegas.
- [x] CRUD de productos.
- [x] Consulta de stock bajo.
- [x] Registro de movimientos.
- [x] Consulta de movimientos.
- [x] Consulta por fechas.
- [x] Auditoría.
- [x] Reporte/resumen de movimientos.
- [x] Documentación mediante Swagger.
- [x] Pruebas mediante Postman.
- [x] Frontend básico HTML/CSS/JS.
- [x] Integración frontend con API REST.

---

# 👨‍💻 Autor

**Andrés Castellanos**

Proyecto académico desarrollado utilizando tecnologías Java y Spring Boot.

---

# 📌 Nota

Para ejecutar correctamente el proyecto se debe:

1. Crear la base de datos utilizando `logitrack_db.sql`.
2. Configurar las credenciales de MySQL.
3. Configurar la clave JWT.
4. Ejecutar el backend Spring Boot.
5. Acceder a Swagger para probar la API.
6. Iniciar sesión y obtener el token JWT.
7. Autorizar las peticiones protegidas.
8. Ejecutar las pruebas de los diferentes módulos.
9. Ejecutar el frontend para comprobar el consumo de la API.
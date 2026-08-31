-- =========================================================
-- LogiTrack S.A. - Script de creación de base de datos
-- Proyecto educativo: Spring Boot + JWT + Auditoría
-- Motor: MySQL 8+
-- =========================================================

CREATE DATABASE IF NOT EXISTS logitrack_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE logitrack_db;

-- ---------------------------------------------------------
-- Limpieza (útil en desarrollo, para poder re-ejecutar)
-- ---------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS auditoria;
DROP TABLE IF EXISTS movimiento_detalle;
DROP TABLE IF EXISTS movimientos;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS bodegas;
DROP TABLE IF EXISTS usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------
-- 1. USUARIOS (login + roles para Spring Security / JWT)
-- ---------------------------------------------------------
CREATE TABLE usuarios (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,      -- se guarda hasheada (BCrypt)
    nombre_completo VARCHAR(100),
    rol             ENUM('ADMIN', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
    activo          BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------
-- 2. BODEGAS
-- ---------------------------------------------------------
CREATE TABLE bodegas (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    ubicacion  VARCHAR(150) NOT NULL,
    capacidad  INT NOT NULL,
    encargado  VARCHAR(100)
);

-- ---------------------------------------------------------
-- 3. PRODUCTOS
-- ---------------------------------------------------------
CREATE TABLE productos (
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    stock     INT NOT NULL DEFAULT 0,
    precio    DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

-- ---------------------------------------------------------
-- 4. MOVIMIENTOS (cabecera: entrada / salida / transferencia)
--    bodega_origen se usa en SALIDA y TRANSFERENCIA
--    bodega_destino se usa en ENTRADA y TRANSFERENCIA
-- ---------------------------------------------------------
CREATE TABLE movimientos (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento  ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    usuario_id       BIGINT NOT NULL,
    bodega_origen_id  BIGINT NULL,
    bodega_destino_id BIGINT NULL,

    CONSTRAINT fk_mov_usuario  FOREIGN KEY (usuario_id)       REFERENCES usuarios(id),
    CONSTRAINT fk_mov_origen   FOREIGN KEY (bodega_origen_id)  REFERENCES bodegas(id),
    CONSTRAINT fk_mov_destino  FOREIGN KEY (bodega_destino_id) REFERENCES bodegas(id)
);

-- ---------------------------------------------------------
-- 5. MOVIMIENTO_DETALLE (productos y cantidades por movimiento)
--    Un movimiento puede incluir varios productos -> relación 1:N
-- ---------------------------------------------------------
CREATE TABLE movimiento_detalle (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    movimiento_id BIGINT NOT NULL,
    producto_id   BIGINT NOT NULL,
    cantidad      INT NOT NULL,

    CONSTRAINT fk_det_movimiento FOREIGN KEY (movimiento_id) REFERENCES movimientos(id) ON DELETE CASCADE,
    CONSTRAINT fk_det_producto   FOREIGN KEY (producto_id)   REFERENCES productos(id)
);

-- ---------------------------------------------------------
-- 6. AUDITORIA (registro automático de cambios INSERT/UPDATE/DELETE)
--    valor_anterior / valor_nuevo se guardan como JSON en texto,
--    para mantenerlo simple (no se crea una tabla por entidad).
-- ---------------------------------------------------------
CREATE TABLE auditoria (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_operacion    ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    fecha_hora        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_id        BIGINT NULL,
    entidad_afectada  VARCHAR(100) NOT NULL,   -- ej: "Producto", "Bodega"
    entidad_id        BIGINT,                  -- id del registro afectado
    valor_anterior    JSON NULL,
    valor_nuevo       JSON NULL,

    CONSTRAINT fk_aud_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ---------------------------------------------------------
-- Índices útiles para las consultas avanzadas del punto 6
-- ---------------------------------------------------------
CREATE INDEX idx_productos_stock       ON productos(stock);
CREATE INDEX idx_movimientos_fecha     ON movimientos(fecha);
CREATE INDEX idx_movimientos_tipo      ON movimientos(tipo_movimiento);
CREATE INDEX idx_auditoria_usuario     ON auditoria(usuario_id);
CREATE INDEX idx_auditoria_tipo        ON auditoria(tipo_operacion);

-- =========================================================
-- DATOS DE PRUEBA (data.sql)
-- =========================================================

-- Contraseñas de ejemplo ya hasheadas con BCrypt para "123456"
-- (genera las tuyas reales desde Spring al registrar usuarios)
INSERT INTO usuarios (username, password, nombre_completo, rol) VALUES
('admin',   '$2a$10$7EqJtq98hPqEX7fNZaFWoOa1J4V4X5g2b5g5D0aA9wFhF6M3s0m0K', 'Administrador General', 'ADMIN'),
('jperez',  '$2a$10$7EqJtq98hPqEX7fNZaFWoOa1J4V4X5g2b5g5D0aA9wFhF6M3s0m0K', 'Juan Pérez',            'EMPLEADO');

INSERT INTO bodegas (nombre, ubicacion, capacidad, encargado) VALUES
('Bodega Central',    'Bogotá',        5000, 'Carlos Gómez'),
('Bodega Norte',      'Bucaramanga',   3000, 'Ana Rodríguez'),
('Bodega Occidente',  'Medellín',      4000, 'Luis Torres');

INSERT INTO productos (nombre, categoria, stock, precio) VALUES
('Laptop Lenovo V15',      'Tecnología', 25, 1800000.00),
('Mouse Inalámbrico',      'Tecnología', 5,  35000.00),
('Silla Ergonómica',       'Muebles',    12, 250000.00),
('Escritorio Ejecutivo',   'Muebles',    3,  600000.00),
('Cable HDMI 2m',          'Tecnología', 8,  15000.00);

-- Movimiento de ENTRADA (llega mercancía a la bodega destino)
INSERT INTO movimientos (tipo_movimiento, usuario_id, bodega_destino_id) VALUES
('ENTRADA', 1, 1);
INSERT INTO movimiento_detalle (movimiento_id, producto_id, cantidad) VALUES
(1, 1, 10),
(1, 2, 20);

-- Movimiento de SALIDA (sale mercancía desde la bodega origen)
INSERT INTO movimientos (tipo_movimiento, usuario_id, bodega_origen_id) VALUES
('SALIDA', 2, 1);
INSERT INTO movimiento_detalle (movimiento_id, producto_id, cantidad) VALUES
(2, 2, 15);

-- Movimiento de TRANSFERENCIA (entre dos bodegas)
INSERT INTO movimientos (tipo_movimiento, usuario_id, bodega_origen_id, bodega_destino_id) VALUES
('TRANSFERENCIA', 1, 1, 2);
INSERT INTO movimiento_detalle (movimiento_id, producto_id, cantidad) VALUES
(3, 3, 4);

-- Ejemplo de registro de auditoría
INSERT INTO auditoria (tipo_operacion, usuario_id, entidad_afectada, entidad_id, valor_anterior, valor_nuevo) VALUES
('UPDATE', 1, 'Producto', 2, '{"stock": 20}', '{"stock": 5}');

-- =========================================================
-- CONSULTAS DE EJEMPLO (para probar los reportes del punto 6)
-- =========================================================

-- Productos con stock bajo (< 10 unidades)
-- SELECT * FROM productos WHERE stock < 10;

-- Movimientos por rango de fechas
-- SELECT * FROM movimientos WHERE fecha BETWEEN '2026-01-01' AND '2026-12-31';

-- Auditorías por usuario
-- SELECT * FROM auditoria WHERE usuario_id = 1;

-- Auditorías por tipo de operación
-- SELECT * FROM auditoria WHERE tipo_operacion = 'UPDATE';

-- Stock total por bodega (a partir de movimientos ENTRADA/SALIDA/TRANSFERENCIA)
-- (referencial: en el backend normalmente se mantiene un stock por bodega
--  con una tabla intermedia bodega_producto; aquí se deja el stock global
--  en productos para simplificar el proyecto educativo)

-- Productos más movidos (top productos por cantidad total movida)
-- SELECT p.nombre, SUM(md.cantidad) AS total_movido
-- FROM movimiento_detalle md
-- JOIN productos p ON p.id = md.producto_id
-- GROUP BY p.nombre
-- ORDER BY total_movido DESC;
-- ============================================================
-- BASE DE DATOS: LOGITRACK
-- ============================================================

CREATE DATABASE IF NOT EXISTS logitrack_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE logitrack_db;


-- ============================================================
-- TABLA: USUARIOS
-- ============================================================

CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100),
    rol ENUM('ADMIN', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
    activo TINYINT(1) NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_username (username)
) ENGINE=InnoDB;


-- ============================================================
-- TABLA: BODEGAS
-- ============================================================

CREATE TABLE bodegas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    capacidad INT NOT NULL,
    encargado VARCHAR(100),

    PRIMARY KEY (id)
) ENGINE=InnoDB;


-- ============================================================
-- TABLA: PRODUCTOS
-- ============================================================

CREATE TABLE productos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    stock INT NOT NULL DEFAULT 0,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    PRIMARY KEY (id)
) ENGINE=InnoDB;


-- ============================================================
-- TABLA: MOVIMIENTOS
-- ============================================================

CREATE TABLE movimientos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    usuario_id BIGINT NOT NULL,
    bodega_origen_id BIGINT DEFAULT NULL,
    bodega_destino_id BIGINT DEFAULT NULL,

    PRIMARY KEY (id),

    KEY fk_mov_usuario (usuario_id),
    KEY fk_mov_origen (bodega_origen_id),
    KEY fk_mov_destino (bodega_destino_id),

    CONSTRAINT fk_mov_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id),

    CONSTRAINT fk_mov_origen
        FOREIGN KEY (bodega_origen_id)
        REFERENCES bodegas(id),

    CONSTRAINT fk_mov_destino
        FOREIGN KEY (bodega_destino_id)
        REFERENCES bodegas(id)
) ENGINE=InnoDB;


-- ============================================================
-- TABLA: MOVIMIENTO_DETALLE
-- ============================================================

CREATE TABLE movimiento_detalle (
    id BIGINT NOT NULL AUTO_INCREMENT,
    movimiento_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,

    PRIMARY KEY (id),

    KEY fk_det_movimiento (movimiento_id),
    KEY fk_det_producto (producto_id),

    CONSTRAINT fk_det_movimiento
        FOREIGN KEY (movimiento_id)
        REFERENCES movimientos(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_det_producto
        FOREIGN KEY (producto_id)
        REFERENCES productos(id)
) ENGINE=InnoDB;


-- ============================================================
-- TABLA: AUDITORIA
-- ============================================================

CREATE TABLE auditoria (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tipo_operacion VARCHAR(20) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    usuario_id BIGINT DEFAULT NULL,
    entidad_afectada VARCHAR(100) NOT NULL,
    entidad_id BIGINT DEFAULT NULL,
    valor_anterior TEXT,
    valor_nuevo TEXT,

    PRIMARY KEY (id),

    KEY fk_auditoria_usuario (usuario_id),

    CONSTRAINT fk_auditoria_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
) ENGINE=InnoDB;


-- ============================================================
-- DATOS INICIALES
-- ============================================================


-- ============================================================
-- USUARIOS
-- ============================================================

INSERT INTO usuarios
(username, password, nombre, rol, activo)
VALUES
(
    'admin1',
    '$2a$10$7u1TzEjh40uJ97D0EjslceEXw5UrPJU8Ljlh.UUI4N/c1nnz.6oEa',
    'Administrador',
    'ADMIN',
    1
),
(
    'empleado1',
    '$2a$10$7GXyGuGthVlBeVKjMMUviujxCejOS/fRpJQcJMrW69qoaJh6OZaxS',
    'Empleado General',
    'EMPLEADO',
    1
);


-- ============================================================
-- BODEGAS
-- ============================================================

INSERT INTO bodegas
(nombre, ubicacion, capacidad, encargado)
VALUES
(
    'Bodega Principal',
    'Bucaramanga',
    1500,
    'Carlos Ramirez'
),
(
    'Bodega Norte',
    'Bucaramanga',
    1000,
    'Ana Rodriguez'
),
(
    'Bodega Floridablanca',
    'Floridablanca',
    800,
    'Juan Perez'
);


-- ============================================================
-- PRODUCTOS
-- ============================================================

INSERT INTO productos
(nombre, categoria, stock, precio)
VALUES
(
    'Laptop Lenovo V15',
    'Tecnologia',
    25,
    1800000.00
),
(
    'Mouse Inalambrico',
    'Perifericos',
    15,
    45000.00
),
(
    'Teclado Mecanico',
    'Perifericos',
    20,
    120000.00
),
(
    'Silla Ergonomica',
    'Muebles',
    12,
    250000.00
),
(
    'Cable HDMI 2m',
    'Tecnologia',
    8,
    15000.00
);


-- ============================================================
-- MOVIMIENTOS
-- ============================================================

INSERT INTO movimientos
(fecha, tipo_movimiento, usuario_id, bodega_origen_id, bodega_destino_id)
VALUES
(
    '2026-09-01 09:00:00',
    'ENTRADA',
    1,
    NULL,
    1
),
(
    '2026-09-01 10:00:00',
    'TRANSFERENCIA',
    1,
    1,
    2
),
(
    '2026-09-01 11:00:00',
    'SALIDA',
    2,
    2,
    NULL
);


-- ============================================================
-- DETALLE DE MOVIMIENTOS
-- ============================================================

INSERT INTO movimiento_detalle
(movimiento_id, producto_id, cantidad)
VALUES
(1, 1, 10),
(1, 2, 20),
(2, 1, 5),
(2, 3, 10),
(3, 2, 5);


-- ============================================================
-- AUDITORIA
-- ============================================================

INSERT INTO auditoria
(tipo_operacion, fecha_hora, usuario_id, entidad_afectada, entidad_id, valor_anterior, valor_nuevo)
VALUES
(
    'INSERT',
    '2026-09-01 09:00:00',
    1,
    'Bodega',
    1,
    NULL,
    'Bodega Principal - Bucaramanga'
),
(
    'INSERT',
    '2026-09-01 09:30:00',
    1,
    'Producto',
    1,
    NULL,
    'Laptop Lenovo V15'
);


-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
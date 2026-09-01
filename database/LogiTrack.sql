CREATE DATABASE  IF NOT EXISTS `logitrack_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `logitrack_db`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: logitrack_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `auditoria`
--

DROP TABLE IF EXISTS `auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditoria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo_operacion` varchar(20) NOT NULL,
  `fecha_hora` datetime NOT NULL,
  `usuario_id` bigint DEFAULT NULL,
  `entidad_afectada` varchar(100) NOT NULL,
  `entidad_id` bigint DEFAULT NULL,
  `valor_anterior` text,
  `valor_nuevo` text,
  PRIMARY KEY (`id`),
  KEY `fk_auditoria_usuario` (`usuario_id`),
  CONSTRAINT `fk_auditoria_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria`
--

LOCK TABLES `auditoria` WRITE;
/*!40000 ALTER TABLE `auditoria` DISABLE KEYS */;
INSERT INTO `auditoria` VALUES (1,'INSERT','2026-08-31 20:26:19',3,'Bodega',7,NULL,'id=7, nombre=Bodega Norte, ubicacion=Bucaramanga, capacidad=200, encargado=Arturo Ojeda'),(2,'INSERT','2026-08-31 20:26:27',3,'Bodega',8,NULL,'id=8, nombre=Bodega Sur, ubicacion=Girón, capacidad=150, encargado=Andrés Castellanos'),(3,'INSERT','2026-08-31 20:27:25',3,'Producto',11,NULL,'id=11, nombre=Mouse inalámbrico, categoria=Periféricos, stock=0, precio=45000'),(4,'INSERT','2026-08-31 20:27:41',3,'Producto',12,NULL,'id=12, nombre=Teclado mecánico, categoria=Periféricos, stock=5, precio=120000'),(5,'INSERT','2026-08-31 20:28:36',3,'Movimiento',5,NULL,'id=5, fecha=2026-08-31T20:28:36.166341900, tipoMovimiento=ENTRADA, usuario=3, bodegaOrigen=null, bodegaDestino=7'),(6,'INSERT','2026-08-31 20:29:05',3,'Movimiento',6,NULL,'id=6, fecha=2026-08-31T20:29:04.851071500, tipoMovimiento=TRANSFERENCIA, usuario=3, bodegaOrigen=7, bodegaDestino=8'),(7,'INSERT','2026-08-31 20:29:23',3,'Movimiento',7,NULL,'id=7, fecha=2026-08-31T20:29:22.586879500, tipoMovimiento=SALIDA, usuario=3, bodegaOrigen=8, bodegaDestino=null'),(8,'INSERT','2026-08-31 20:44:46',3,'Bodega',9,NULL,'id=9, nombre=Bodega Bucaramanga, ubicacion=Bucaramanga, capacidad=1000, encargado=Carlos Ramirez'),(9,'INSERT','2026-08-31 20:45:14',3,'Bodega',10,NULL,'id=10, nombre=Bodega Floridablanca, ubicacion=Floridablanca, capacidad=800, encargado=Juan Perez'),(10,'INSERT','2026-08-31 20:45:29',3,'Bodega',11,NULL,'id=11, nombre=Bodega Giron, ubicacion=Giron, capacidad=600, encargado=Pedro Gomez'),(11,'UPDATE','2026-08-31 20:47:39',3,'Bodega',1,'id=1, nombre=Bodega Central, ubicacion=Bogotá, capacidad=5000, encargado=Carlos Gómez','id=1, nombre=Bodega Principal Bucaramanga, ubicacion=Bucaramanga, capacidad=1500, encargado=Carlos Ramirez'),(12,'UPDATE','2026-08-31 20:47:41',3,'Bodega',1,'id=1, nombre=Bodega Principal Bucaramanga, ubicacion=Bucaramanga, capacidad=1500, encargado=Carlos Ramirez','id=1, nombre=Bodega Principal Bucaramanga, ubicacion=Bucaramanga, capacidad=1500, encargado=Carlos Ramirez'),(13,'INSERT','2026-09-01 15:18:46',3,'Producto',13,NULL,'id=13, nombre=Laptop Lenovo V14, categoria=Tecnologia, stock=32, precio=1500000'),(14,'INSERT','2026-09-01 15:19:15',3,'Movimiento',8,NULL,'id=8, fecha=2026-09-01T15:19:15.122853900, tipoMovimiento=ENTRADA, usuario=3, bodegaOrigen=null, bodegaDestino=1');
/*!40000 ALTER TABLE `auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bodegas`
--

DROP TABLE IF EXISTS `bodegas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bodegas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `ubicacion` varchar(150) NOT NULL,
  `capacidad` int NOT NULL,
  `encargado` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bodegas`
--

LOCK TABLES `bodegas` WRITE;
/*!40000 ALTER TABLE `bodegas` DISABLE KEYS */;
INSERT INTO `bodegas` VALUES (1,'Bodega Principal Bucaramanga','Bucaramanga',1500,'Carlos Ramirez'),(2,'Bodega Norte','Bucaramanga',3000,'Ana Rodríguez'),(3,'Bodega Occidente','Medellín',4000,'Luis Torres'),(4,'Bodega Central','Bogotá',5000,'Carlos Gómez'),(5,'Bodega Norte','Bucaramanga',3000,'Ana Rodríguez'),(6,'Bodega Occidente','Medellín',4000,'Luis Torres'),(7,'Bodega Norte','Bucaramanga',200,'Arturo Ojeda'),(8,'Bodega Sur','Girón',150,'Andrés Castellanos'),(9,'Bodega Bucaramanga','Bucaramanga',1000,'Carlos Ramirez'),(10,'Bodega Floridablanca','Floridablanca',800,'Juan Perez'),(11,'Bodega Giron','Giron',600,'Pedro Gomez');
/*!40000 ALTER TABLE `bodegas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimiento_detalle`
--

DROP TABLE IF EXISTS `movimiento_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimiento_detalle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `movimiento_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_det_movimiento` (`movimiento_id`),
  KEY `fk_det_producto` (`producto_id`),
  CONSTRAINT `fk_det_movimiento` FOREIGN KEY (`movimiento_id`) REFERENCES `movimientos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_det_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimiento_detalle`
--

LOCK TABLES `movimiento_detalle` WRITE;
/*!40000 ALTER TABLE `movimiento_detalle` DISABLE KEYS */;
INSERT INTO `movimiento_detalle` VALUES (5,5,11,50),(6,5,12,20),(7,6,11,15),(8,7,11,5),(9,8,13,35);
/*!40000 ALTER TABLE `movimiento_detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimientos`
--

DROP TABLE IF EXISTS `movimientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimientos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tipo_movimiento` enum('ENTRADA','SALIDA','TRANSFERENCIA') NOT NULL,
  `usuario_id` bigint NOT NULL,
  `bodega_origen_id` bigint DEFAULT NULL,
  `bodega_destino_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mov_usuario` (`usuario_id`),
  KEY `fk_mov_origen` (`bodega_origen_id`),
  KEY `fk_mov_destino` (`bodega_destino_id`),
  CONSTRAINT `fk_mov_destino` FOREIGN KEY (`bodega_destino_id`) REFERENCES `bodegas` (`id`),
  CONSTRAINT `fk_mov_origen` FOREIGN KEY (`bodega_origen_id`) REFERENCES `bodegas` (`id`),
  CONSTRAINT `fk_mov_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimientos`
--

LOCK TABLES `movimientos` WRITE;
/*!40000 ALTER TABLE `movimientos` DISABLE KEYS */;
INSERT INTO `movimientos` VALUES (3,'2026-08-28 08:53:16','ENTRADA',1,NULL,1),(4,'2026-08-28 08:53:53','SALIDA',2,1,NULL),(5,'2026-08-31 20:28:36','ENTRADA',3,NULL,7),(6,'2026-08-31 20:29:05','TRANSFERENCIA',3,7,8),(7,'2026-08-31 20:29:23','SALIDA',3,8,NULL),(8,'2026-09-01 15:19:15','ENTRADA',3,NULL,1);
/*!40000 ALTER TABLE `movimientos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `precio` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'Laptop Lenovo V15','Tecnología',25,1800000.00),(2,'Mouse Inalámbrico','Tecnología',5,35000.00),(3,'Silla Ergonómica','Muebles',12,250000.00),(4,'Escritorio Ejecutivo','Muebles',3,600000.00),(5,'Cable HDMI 2m','Tecnología',8,15000.00),(6,'Laptop Lenovo V15','Tecnología',25,1800000.00),(7,'Mouse Inalámbrico','Tecnología',5,35000.00),(8,'Silla Ergonómica','Muebles',12,250000.00),(9,'Escritorio Ejecutivo','Muebles',3,600000.00),(10,'Cable HDMI 2m','Tecnología',8,15000.00),(11,'Mouse inalámbrico','Periféricos',45,45000.00),(12,'Teclado mecánico','Periféricos',25,120000.00),(13,'Laptop Lenovo V14','Tecnologia',67,1500000.00);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `rol` enum('ADMIN','EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'admin','123456789','Administrador General','ADMIN',1,'2026-08-28 08:44:01'),(2,'jperez','123456789','Juan Pérez','EMPLEADO',1,'2026-08-28 08:44:01'),(3,'admin1','$2a$10$7u1TzEjh40uJ97D0EjslceEXw5UrPJU8Ljlh.UUI4N/c1nnz.6oEa','Admin','ADMIN',1,'2026-08-31 16:33:39'),(4,'Aojeda','$2a$10$7GXyGuGthVlBeVKjMMUviujxCejOS/fRpJQcJMrW69qoaJh6OZaxS','Arturo Ojeda','EMPLEADO',1,'2026-08-31 19:53:46');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-01 17:03:50

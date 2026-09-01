# Guía de pruebas — LogiTrack S.A.

## 1. Login — obtener JWT

### POST
`POST http://localhost:8080/auth/login`

### Body
```json
{
    "username": "admin1",
    "password": "123456"
}
```

Copia el token recibido.

En Postman:
- Authorization → Bearer Token
- Pega únicamente el JWT.

---

## 2. Swagger

Abre:

`http://localhost:8080/swagger-ui/index.html`

Si no funciona, prueba:

`http://localhost:8080/swagger-ui.html`

Pulsa **Authorize** y coloca:

```text
Bearer TU_TOKEN
```

---

# 3. Bodegas

## 3.1 Crear bodega

### POST
`POST /bodegas`

```json
{
    "nombre": "Bodega Bucaramanga",
    "ubicacion": "Bucaramanga",
    "capacidad": 1000,
    "encargado": "Carlos Ramirez"
}
```

## 3.2 Crear segunda bodega

### POST
`POST /bodegas`

```json
{
    "nombre": "Bodega Floridablanca",
    "ubicacion": "Floridablanca",
    "capacidad": 800,
    "encargado": "Juan Perez"
}
```

## 3.3 Crear tercera bodega

### POST
`POST /bodegas`

```json
{
    "nombre": "Bodega Giron",
    "ubicacion": "Giron",
    "capacidad": 600,
    "encargado": "Pedro Gomez"
}
```

## 3.4 Consultar todas

### GET
`GET /bodegas`

## 3.5 Consultar por ID

### GET
`GET /bodegas/1`

## 3.6 Actualizar

### PUT
`PUT /bodegas/1`

```json
{
    "nombre": "Bodega Principal Bucaramanga",
    "ubicacion": "Bucaramanga",
    "capacidad": 1500,
    "encargado": "Carlos Ramirez"
}
```

Después revisa la tabla `auditoria`.

Debe existir un registro `UPDATE` con:
- entidad: `Bodega`
- entidad_id: `1`
- valor anterior: datos antes del cambio
- valor nuevo: datos después del cambio
- usuario: `admin1`

## 3.7 Eliminar

No elimines todavía las bodegas 1 y 2, porque se usarán para movimientos.

Para probar DELETE, crea una bodega adicional y elimínala.

### DELETE
`DELETE /bodegas/{id}`

Después revisa la auditoría.

---

# 4. Productos

## 4.1 Crear producto — Laptop

### POST
`POST /productos`

```json
{
    "nombre": "Laptop Lenovo",
    "categoria": "Tecnologia",
    "stock": 25,
    "precio": 2500000
}
```

## 4.2 Crear producto — Mouse

### POST
`POST /productos`

```json
{
    "nombre": "Mouse Logitech",
    "categoria": "Perifericos",
    "stock": 50,
    "precio": 80000
}
```

## 4.3 Crear producto — stock bajo

### POST
`POST /productos`

```json
{
    "nombre": "Teclado Logitech",
    "categoria": "Perifericos",
    "stock": 7,
    "precio": 120000
}
```

Este producto servirá para probar el filtro de stock bajo (`< 10`).

## 4.4 Consultar todos

### GET
`GET /productos`

## 4.5 Consultar por ID

### GET
`GET /productos/1`

## 4.6 Actualizar producto

### PUT
`PUT /productos/1`

```json
{
    "nombre": "Laptop Lenovo ThinkPad",
    "categoria": "Tecnologia",
    "stock": 30,
    "precio": 2800000
}
```

Después revisa `auditoria`.

Debe aparecer un `UPDATE` con los valores anteriores y nuevos.

## 4.7 Probar DELETE

Crea un producto adicional:

```json
{
    "nombre": "Producto Prueba",
    "categoria": "Prueba",
    "stock": 5,
    "precio": 10000
}
```

Después elimina el ID generado:

### DELETE
`DELETE /productos/{id}`

Revisa que aparezca un registro `DELETE` en `auditoria`.

---

# 5. Movimientos de inventario

> **Importante:** antes de probar esta sección, revisa tu `MovimientoRequest`, porque los nombres exactos de los campos dependen de tu código actual.

La idea es probar:
- ENTRADA
- SALIDA
- TRANSFERENCIA

Ejemplos orientativos:

## 5.1 ENTRADA

```json
{
    "tipoMovimiento": "ENTRADA",
    "bodegaDestinoId": 1,
    "productos": [
        {
            "productoId": 1,
            "cantidad": 10
        }
    ]
}
```

## 5.2 SALIDA

```json
{
    "tipoMovimiento": "SALIDA",
    "bodegaOrigenId": 1,
    "productos": [
        {
            "productoId": 1,
            "cantidad": 5
        }
    ]
}
```

## 5.3 TRANSFERENCIA

```json
{
    "tipoMovimiento": "TRANSFERENCIA",
    "bodegaOrigenId": 1,
    "bodegaDestinoId": 2,
    "productos": [
        {
            "productoId": 1,
            "cantidad": 3
        }
    ]
}
```

**No copies estos JSON si los nombres de tu `MovimientoRequest` son diferentes.**

---

# 6. Auditoría

## 6.1 Consultar todas

### GET
`GET /auditorias`

Comprueba que existan registros de:
- `INSERT`
- `UPDATE`
- `DELETE`

y que tengan usuario y fecha.

## 6.2 Consultar por usuario

### GET
`GET /auditorias/usuario/1`

Usa el ID real de `admin1`.

## 6.3 Consultar por tipo

### GET
`GET /auditorias/tipo/INSERT`

### GET
`GET /auditorias/tipo/UPDATE`

### GET
`GET /auditorias/tipo/DELETE`

---

# 7. Reportes y filtros

## 7.1 Productos con stock bajo

Busca el endpoint correspondiente en Swagger.

La prueba debe devolver productos con:

`stock < 10`

Por ejemplo:

`Teclado Logitech → stock 7`

## 7.2 Movimientos por rango de fechas

Busca en Swagger el endpoint de movimientos por fechas.

Usa un rango que incluya los movimientos que acabas de crear.

## 7.3 Reporte general

Busca en Swagger el endpoint de resumen general.

Debe incluir:
- stock total por bodega
- productos más movidos

---

# 8. Pruebas de errores

## 8.1 Producto inexistente

### GET
`GET /productos/9999`

Esperado:

`404 NOT FOUND`

## 8.2 Bodega inexistente

### GET
`GET /bodegas/9999`

Esperado:

`404 NOT FOUND`

## 8.3 Petición sin JWT

Elimina el Authorization de Postman y prueba:

### GET
`GET /productos`

Esperado:

`401 UNAUTHORIZED`

## 8.4 Datos inválidos

Prueba crear un producto con datos inválidos:

```json
{
    "nombre": "",
    "categoria": "",
    "stock": -5,
    "precio": -100
}
```

Esperado:

`400 BAD REQUEST`

---

# 9. Probar usuario EMPLEADO

Crea un empleado si tu endpoint lo permite:

### POST
`POST /auth/register`

```json
{
    "username": "empleado1",
    "password": "123456",
    "rol": "EMPLEADO"
}
```

Haz login:

### POST
`POST /auth/login`

```json
{
    "username": "empleado1",
    "password": "123456"
}
```

Usa el nuevo JWT para probar las rutas según las restricciones de roles de tu `SecurityConfig`.

---

# 10. Orden recomendado de pruebas

1. `POST /auth/login`
2. Configurar JWT en Postman
3. Configurar JWT en Swagger
4. `POST /bodegas`
5. Crear 2 bodegas adicionales
6. `GET /bodegas`
7. `GET /bodegas/{id}`
8. `PUT /bodegas/{id}`
9. Revisar auditoría del UPDATE
10. `POST /productos`
11. Crear los 3 productos
12. `GET /productos`
13. `GET /productos/{id}`
14. `PUT /productos/{id}`
15. Revisar auditoría del UPDATE
16. Crear producto de prueba
17. `DELETE /productos/{id}`
18. Revisar auditoría del DELETE
19. Probar ENTRADA
20. Probar SALIDA
21. Probar TRANSFERENCIA
22. `GET /auditorias`
23. Auditoría por usuario
24. Auditoría por tipo
25. Productos con stock bajo
26. Movimientos por rango de fechas
27. Reporte general
28. Probar `401`
29. Probar `404`
30. Probar `400`
31. Probar permisos de EMPLEADO

---

# 11. Checklist final

- [ ] Login funciona
- [ ] JWT funciona
- [ ] Swagger funciona
- [ ] Crear bodegas
- [ ] Consultar bodegas
- [ ] Actualizar bodegas
- [ ] Eliminar bodegas
- [ ] Auditoría de bodegas
- [ ] Crear productos
- [ ] Consultar productos
- [ ] Actualizar productos
- [ ] Eliminar productos
- [ ] Auditoría de productos
- [ ] Entrada de inventario
- [ ] Salida de inventario
- [ ] Transferencia
- [ ] Auditoría de movimientos
- [ ] Stock bajo
- [ ] Movimientos por fechas
- [ ] Auditorías por usuario
- [ ] Auditorías por operación
- [ ] Reporte general
- [ ] Errores 400
- [ ] Errores 401
- [ ] Errores 404
- [ ] Roles ADMIN/EMPLEADO
- [ ] Swagger documentado
- [ ] Base de datos funcionando

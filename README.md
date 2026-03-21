# 🛒 SalesFlow API

Sistema backend para gestión de ventas, inventario y tiendas multi-rol desarrollado con **Spring Boot**.

---

## 🚀 Descripción

SalesFlow es una API REST que permite gestionar:

- Usuarios con roles (OWNER, EMPLOYEE, CLIENT)
- Tiendas
- Productos
- Ventas
- Inventario
- Tickets (recibos)

El sistema soporta tanto:

✔ Ventas en tienda física (empleado)  
✔ Ventas en línea (cliente)  

Incluye control de inventario automático y seguridad basada en JWT.

---

## 🧠 Características principales

- 🔐 Autenticación con JWT
- 👥 Control de acceso por roles
- 🏪 Aislamiento por tienda
- 📦 Gestión de productos e inventario
- 💰 Registro de ventas
- ❌ Cancelación de ventas con restauración de stock
- 📊 Historial de movimientos de inventario
- 🧾 Generación de tickets
- 📚 Documentación con Swagger
- 🧪 Tests unitarios
- 🐳 Base de datos con Docker (PostgreSQL)

---

## 🏗️ Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring Security
- JWT (jjwt)
- PostgreSQL
- Docker
- JPA / Hibernate
- Swagger / OpenAPI
- JUnit + Mockito

---

## ⚙️ Instalación y ejecución
### 1. Clonar el repositorio

```bash
git clone https://github.com/delia-20/salesflow-backend.git
cd salesflow
```
### 2.Levantar PostgreSQL con Docker en bash
```
docker compose up -d
```
### 3. Configurar application.properties
spring.datasource.url=jdbc:postgresql://localhost:5433/salesflow_local
spring.datasource.username=default_user
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always


### 4. Ejecutar el proyecto
```
./mvnw spring-boot:run

```
## 📚 Documentación API

Disponible en: ```
http://localhost:8080/swagger-ui.html
```
--- 
##🔐 Autenticación
```
Login
```

POST /api/v1/login
```

```
Request:

{
  "username": "admin",
  "password": "admin123"
}

```

```
Response:

{
  "token": "jwt-token",
  "user": {
    "id": "...",
    "username": "admin",
    "role": "OWNER"
  }
}
```
---
## 👥 Roles del sistema
### 🟢 OWNER

Crear tiendas

Gestionar empleados

Ver ventas e inventario

Administrar productos

### 🔵 EMPLOYEE

Realizar ventas

Gestionar inventario

Consultar productos de su tienda

### 🟣 CLIENT

Realizar compras

Consultar historial de compras

### 🔄 Flujo de venta

Se envía una lista de productos

Se valida stock

Se calcula total automáticamente

Se descuenta inventario

Se registra la venta

Se genera ticket

Se guarda movimiento de inventario

### ❌ Cancelación de venta

Restaura el stock automáticamente

Genera movimiento de inventario tipo CANCEL_SALE


## 📦 Tipos de movimientos de inventario

SALE → Venta

RESTOCK → Reabastecimiento

ADJUSTMENT → Ajuste manual

CANCEL_SALE → Cancelación de venta

ADD → Nuevo producto

## 🧪 Testing

Se incluyen pruebas unitarias para:

AuthService

SaleService

InventoryService

ProductService

Ejecutar tests:
```
./mvnw test

```
--- 
## 📌 Notas

El sistema utiliza UUID como identificadores

No se utilizan relaciones JPA complejas (diseño desacoplado)

Seguridad basada en tokens JWT

Inventario se gestiona automáticamente en cada operación

## 🚀 Roadmap (V2)

Paginación

Multi-store completo

Integración de pagos (Stripe / MercadoPago)

Reportes avanzados

Notificaciones

Frontend

## 👩‍💻 Autor

Desarrollado como proyecto de portafolio backend.

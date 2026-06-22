# 🍔 Food Store - Sistema de Gestión de Pedidos

## 📋 Descripción

Food Store es una aplicación de consola desarrollada en Java que permite administrar categorías, productos, usuarios y pedidos de una tienda de alimentos.

El sistema implementa una arquitectura en capas (Entities, Repository, Service) y utiliza una base de datos relacional para almacenar la información de manera persistente.

---

## 🚀 Funcionalidades

### Categorías

* Crear categorías.
* Listar categorías activas.
* Modificar categorías existentes.
* Eliminación lógica de categorías.

### Productos

* Crear productos.
* Asociar productos a categorías.
* Consultar productos disponibles.
* Modificar información de productos.
* Actualizar stock.
* Eliminación lógica de productos.

### Usuarios

* Registrar usuarios.
* Consultar usuarios activos.
* Modificar datos de usuarios.
* Eliminación lógica de usuarios.
* Gestión de roles.

### Pedidos

* Crear pedidos.
* Asociar pedidos a usuarios.
* Agregar productos al pedido.
* Calcular automáticamente subtotales y total.
* Validar stock disponible.
* Actualizar estado del pedido.
* Registrar forma de pago.
* Consultar pedidos almacenados.

---

## 🏗️ Arquitectura del Proyecto

```text
src/
└── integrado/
    └── prog2/
        ├── config/
        │   └── DatabaseConnection.java
        │
        ├── entities/
        │   ├── Base.java
        │   ├── Calculable.java
        │   ├── Categoria.java
        │   ├── Producto.java
        │   ├── Usuario.java
        │   ├── Pedido.java
        │   └── DetallePedido.java
        │
        ├── enums/
        │   ├── Estado.java
        │   ├── FormaPago.java
        │   └── Rol.java
        │
        ├── exception/
        │   └── ValidacionException.java
        │
        ├── repository/
        │   ├── CategoriaRepository.java
        │   ├── ProductoRepository.java
        │   ├── UsuarioRepository.java
        │   ├── PedidoRepository.java
        │   └── DetallePedidoRepository.java
        │
        ├── service/
        │   ├── CategoriaService.java
        │   ├── ProductoService.java
        │   ├── UsuarioService.java
        │   └── PedidoService.java
        │
        └── Main.java
```

---

## 🧩 Tecnologías Utilizadas

* Java
* JDBC
* PostgreSQl / NeonDB
* Programación Orientada a Objetos (POO)
* Patrón Repository
* Arquitectura en Capas

---

## 📊 Modelo de Negocio

### Categoría

Representa la clasificación de los productos.

**Atributos principales:**

* ID
* Nombre
* Estado (activo/inactivo)

### Producto

Representa los artículos disponibles para la venta.

**Atributos principales:**

* ID
* Nombre
* Precio
* Stock
* Categoría

### Usuario

Representa los clientes o administradores del sistema.

**Atributos principales:**

* ID
* Nombre
* Email
* Rol

### Pedido

Representa una compra realizada por un usuario.

**Atributos principales:**

* ID
* Fecha
* Estado
* Forma de pago
* Total
* Usuario

### DetallePedido

Representa cada producto incluido dentro de un pedido.

**Atributos principales:**

* Producto
* Cantidad
* Subtotal

---

## ⚙️ Reglas de Negocio

### Validación de Stock

Antes de agregar un producto al pedido se verifica que exista stock suficiente.

### Cálculo de Totales

El total del pedido se calcula automáticamente sumando los subtotales de cada detalle.

### Estados del Pedido

Los estados posibles se gestionan mediante el enum `Estado`.

### Formas de Pago

Las formas de pago disponibles se gestionan mediante el enum `FormaPago`.

### Roles de Usuario

Los permisos de usuario se gestionan mediante el enum `Rol`.

---

## ▶️ Ejecución del Proyecto

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
```

### 2. Configurar la base de datos

Modificar los parámetros de conexión en:

```java
DatabaseConnection.java
```

Configurando:

```java
URL
USUARIO
PASSWORD
```

### 3. Compilar el proyecto

```bash
javac *.java
```

### 4. Ejecutar

```bash
java integrado.prog2.Main
```

---

## 📌 Excepciones Personalizadas

El proyecto utiliza la excepción:

```java
ValidacionException
```

La cual centraliza las validaciones de negocio y los mensajes de error para el usuario.

---
## LINKS REPOSITORIO Y VIDEO.

* https://drive.google.com/drive/u/2/folders/1HrJBHdHY2tXS-Q3_T7wYYriDtQeyo7Bw
* https://github.com/ocmedina/FoodStore-UTN
---

## 👥 Integrantes

* MEDINA, Octavio
* MARTÍN, Tomás
* GRIS, Emir
* RAMPONE, Brian

---

## 📚 Materia

Programación II

ProyectoF Integrador – Sistema de Gestión de Pedidos Food Store.
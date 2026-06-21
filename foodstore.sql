CREATE DATABASE IF NOT EXISTS foodstore;
USE foodstore;

CREATE TABLE IF NOT EXISTS categorias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    eliminado   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS usuarios (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    apellido   VARCHAR(100) NOT NULL,
    mail       VARCHAR(150) NOT NULL UNIQUE,
    celular    VARCHAR(30),
    contrasena VARCHAR(255) NOT NULL,
    rol        VARCHAR(20)  NOT NULL DEFAULT 'USUARIO',
    eliminado  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS productos (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100)   NOT NULL,
    descripcion  TEXT,
    precio       DECIMAL(10, 2) NOT NULL,
    stock        INT            NOT NULL DEFAULT 0,
    imagen       TEXT,
    disponible   BOOLEAN        NOT NULL DEFAULT TRUE,
    categoria_id BIGINT,
    eliminado    BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE IF NOT EXISTS pedidos (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha      DATE           NOT NULL,
    estado     VARCHAR(20)    NOT NULL DEFAULT 'PENDIENTE',
    total      DECIMAL(10, 2) NOT NULL DEFAULT 0,
    forma_pago VARCHAR(20)    NOT NULL,
    usuario_id BIGINT,
    eliminado  BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS detalles_pedido (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad    INT            NOT NULL,
    subtotal    DECIMAL(10, 2) NOT NULL,
    pedido_id   BIGINT,
    producto_id BIGINT,
    eliminado   BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

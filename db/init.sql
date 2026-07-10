SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS inventario_ventas
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE inventario_ventas;

CREATE TABLE IF NOT EXISTS producto (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  codigo  VARCHAR(50)  NOT NULL UNIQUE,
  nombre  VARCHAR(150) NOT NULL,
  precio  DECIMAL(10,2) NOT NULL DEFAULT 0,
  stock   INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS venta (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  cliente VARCHAR(150),
  fecha   DATETIME NOT NULL,
  total   DECIMAL(12,2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS detalle_venta (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  venta_id        INT NOT NULL,
  producto_id     INT NOT NULL,
  cantidad        INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_dv_venta    FOREIGN KEY (venta_id)    REFERENCES venta(id),
  CONSTRAINT fk_dv_producto FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Datos de ejemplo
INSERT INTO producto (codigo, nombre, precio, stock) VALUES
  ('P001', 'Arroz Costeño 1kg',      4.50, 120),
  ('P002', 'Aceite Primor 1L',       9.90,  8),
  ('P003', 'Leche Gloria 400g',      3.80, 200),
  ('P004', 'Azúcar Rubia 1kg',       4.20,  5),
  ('P005', 'Fideos Don Vittorio 500g', 2.90, 60)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Usuario de aplicacion
CREATE USER IF NOT EXISTS 'app'@'%' IDENTIFIED BY 'app';
GRANT ALL PRIVILEGES ON inventario_ventas.* TO 'app'@'%';
FLUSH PRIVILEGES;

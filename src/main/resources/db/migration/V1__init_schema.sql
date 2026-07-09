CREATE TABLE IF NOT EXISTS clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(30),
    direccion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio NUMERIC(12,2) NOT NULL,
    stock INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS pedidos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    estado VARCHAR(30) NOT NULL,
    total NUMERIC(12,2) NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_pedidos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS detalle_pedidos (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    CONSTRAINT fk_detalle_pedidos_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    CONSTRAINT fk_detalle_pedidos_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE IF NOT EXISTS ventas (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    total NUMERIC(12,2) NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS detalle_ventas (
    id BIGSERIAL PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    CONSTRAINT fk_detalle_ventas_venta FOREIGN KEY (venta_id) REFERENCES ventas(id),
    CONSTRAINT fk_detalle_ventas_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE IF NOT EXISTS pagos (
    id BIGSERIAL PRIMARY KEY,
    fecha_pago TIMESTAMP NOT NULL,
    monto NUMERIC(12,2) NOT NULL,
    metodo VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    venta_id BIGINT NOT NULL,
    CONSTRAINT fk_pagos_venta FOREIGN KEY (venta_id) REFERENCES ventas(id)
);

CREATE INDEX IF NOT EXISTS idx_pedidos_cliente_id ON pedidos(cliente_id);
CREATE INDEX IF NOT EXISTS idx_detalle_pedidos_pedido_id ON detalle_pedidos(pedido_id);
CREATE INDEX IF NOT EXISTS idx_detalle_pedidos_producto_id ON detalle_pedidos(producto_id);
CREATE INDEX IF NOT EXISTS idx_ventas_cliente_id ON ventas(cliente_id);
CREATE INDEX IF NOT EXISTS idx_detalle_ventas_venta_id ON detalle_ventas(venta_id);
CREATE INDEX IF NOT EXISTS idx_detalle_ventas_producto_id ON detalle_ventas(producto_id);
CREATE INDEX IF NOT EXISTS idx_pagos_venta_id ON pagos(venta_id);

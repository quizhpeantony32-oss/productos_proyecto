package com.productos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}

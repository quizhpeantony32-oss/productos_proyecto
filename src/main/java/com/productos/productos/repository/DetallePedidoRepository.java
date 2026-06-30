package com.productos.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);
}

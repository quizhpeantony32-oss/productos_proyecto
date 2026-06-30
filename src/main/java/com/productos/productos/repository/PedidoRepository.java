package com.productos.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);
}

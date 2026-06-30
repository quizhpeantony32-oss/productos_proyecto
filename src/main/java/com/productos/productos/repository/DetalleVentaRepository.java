package com.productos.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVentaId(Long ventaId);
}

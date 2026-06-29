package com.productos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}

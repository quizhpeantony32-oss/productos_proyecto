package com.productos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

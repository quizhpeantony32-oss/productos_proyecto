package com.productos.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

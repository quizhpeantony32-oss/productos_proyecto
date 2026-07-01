package com.productos.productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productos.productos.entity.Pago;
import com.productos.productos.enums.EstadoPago;
import com.productos.productos.entity.Venta;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByVentaId(Long ventaId);

    List<Pago> findByVentaIdAndEstado(Long ventaId, EstadoPago estado);

}

package com.productos.productos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.productos.productos.entity.Pago;
import com.productos.productos.entity.Venta;
import com.productos.productos.enums.EstadoPago;
import com.productos.productos.enums.MetodoPago;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.PagoRepository;
import com.productos.productos.repository.VentaRepository;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final VentaRepository ventaRepository;

    public PagoService(PagoRepository pagoRepository, VentaRepository ventaRepository) {
        this.pagoRepository = pagoRepository;
        this.ventaRepository = ventaRepository;
    }

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Pago findById(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
    }

    @Transactional
    public Pago registrarPago(Long ventaId, BigDecimal monto, MetodoPago metodo) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + ventaId));

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        Pago pago = new Pago();
        pago.setVenta(venta);
        pago.setMonto(monto);
        pago.setMetodo(metodo);
        pago.setEstado(EstadoPago.PENDIENTE);

        return pagoRepository.save(pago);
    }

    @Transactional
    public Pago aprobarPago(Long pagoId) {
        Pago pago = findById(pagoId);
        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar pagos pendientes");
        }
        pago.setEstado(EstadoPago.APROBADO);
        return pagoRepository.save(pago);
    }

    @Transactional
    public Pago rechazarPago(Long pagoId) {
        Pago pago = findById(pagoId);
        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden rechazar pagos pendientes");
        }
        pago.setEstado(EstadoPago.RECHAZADO);
        return pagoRepository.save(pago);
    }

    public List<Pago> listarPorVenta(Long ventaId) {
        return pagoRepository.findByVentaId(ventaId);
    }

    public List<Pago> listarPorVentaYEstado(Long ventaId, EstadoPago estado) {
        return pagoRepository.findByVentaIdAndEstado(ventaId, estado);
    }
}

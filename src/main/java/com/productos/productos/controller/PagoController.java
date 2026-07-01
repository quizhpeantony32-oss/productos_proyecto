package com.productos.productos.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productos.productos.entity.Pago;
import com.productos.productos.enums.EstadoPago;
import com.productos.productos.enums.MetodoPago;
import com.productos.productos.service.PagoService;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/{id}")
    public Pago findById(@PathVariable Long id) {
        return pagoService.findById(id);
    }

    @GetMapping("/venta/{ventaId}")
    public List<Pago> listarPorVenta(@PathVariable Long ventaId) {
        return pagoService.listarPorVenta(ventaId);
    }

    @GetMapping
    public List<Pago> listarPorVentaYEstado(
            @RequestParam Long ventaId,
            @RequestParam EstadoPago estado) {
        return pagoService.listarPorVentaYEstado(ventaId, estado);
    }

    @PostMapping
    public Pago registrar(
            @RequestParam Long ventaId,
            @RequestParam BigDecimal monto,
            @RequestParam MetodoPago metodo) {
        return pagoService.registrarPago(ventaId, monto, metodo);
    }

    @PutMapping("/{id}/aprobar")
    public Pago aprobar(@PathVariable Long id) {
        return pagoService.aprobarPago(id);
    }

    @PutMapping("/{id}/rechazar")
    public Pago rechazar(@PathVariable Long id) {
        return pagoService.rechazarPago(id);
    }
}

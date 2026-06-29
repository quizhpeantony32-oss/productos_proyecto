package com.productos.productos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productos.productos.entity.DetalleVenta;
import com.productos.productos.service.DetalleVentaService;

@RestController
@RequestMapping("/api/detalle-ventas")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    public List<DetalleVenta> findAll() {
        return detalleVentaService.findAll();
    }

    @GetMapping("/{id}")
    public DetalleVenta findById(@PathVariable Long id) {
        return detalleVentaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<DetalleVenta> create(@RequestBody DetalleVenta detalleVenta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detalleVentaService.create(detalleVenta));
    }

    @PutMapping("/{id}")
    public DetalleVenta update(@PathVariable Long id, @RequestBody DetalleVenta detalleVenta) {
        return detalleVentaService.update(id, detalleVenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        detalleVentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

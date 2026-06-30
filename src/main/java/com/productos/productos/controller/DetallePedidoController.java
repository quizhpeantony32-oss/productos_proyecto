package com.productos.productos.controller;

import java.util.List;

import jakarta.validation.Valid;
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

import com.productos.productos.entity.DetallePedido;
import com.productos.productos.service.DetallePedidoService;

@RestController
@RequestMapping("/api/detalle-pedidos")
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<DetallePedido> findAll() {
        return detallePedidoService.findAll();
    }

    @GetMapping("/{id}")
    public DetallePedido findById(@PathVariable Long id) {
        return detallePedidoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<DetallePedido> create(@Valid @RequestBody DetallePedido detallePedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detallePedidoService.create(detallePedido));
    }

    @PutMapping("/{id}")
    public DetallePedido update(@PathVariable Long id, @Valid @RequestBody DetallePedido detallePedido) {
        return detallePedidoService.update(id, detallePedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        detallePedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

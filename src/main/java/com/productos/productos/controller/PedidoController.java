package com.productos.productos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productos.productos.dto.ActualizarEstadoPedidoRequest;
import com.productos.productos.dto.CrearPedidoRequest;
import com.productos.productos.entity.DetallePedido;
import com.productos.productos.entity.Pedido;
import com.productos.productos.service.DetallePedidoService;
import com.productos.productos.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;

    public PedidoController(PedidoService pedidoService, DetallePedidoService detallePedidoService) {
        this.pedidoService = pedidoService;
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping
    public List<Pedido> findAll() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    public Pedido findById(@PathVariable Long id) {
        return pedidoService.findById(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> findByCliente(@PathVariable Long clienteId) {
        return pedidoService.findByCliente(clienteId);
    }

    @GetMapping("/{id}/detalles")
    public List<DetallePedido> findDetalles(@PathVariable Long id) {
        return detallePedidoService.findByPedido(id);
    }

    @PostMapping
    public ResponseEntity<Pedido> create(@Valid @RequestBody CrearPedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.create(request));
    }

    @PatchMapping("/{id}/estado")
    public Pedido updateEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoPedidoRequest request) {
        return pedidoService.updateEstado(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

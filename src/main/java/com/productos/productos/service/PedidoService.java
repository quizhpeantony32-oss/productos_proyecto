package com.productos.productos.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.productos.entity.Pedido;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.PedidoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    public Pedido create(Pedido pedido) {
        pedido.setId(null);
        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }
        if (pedido.getTotal() == null) {
            pedido.setTotal(BigDecimal.ZERO);
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido update(Long id, Pedido request) {
        Pedido pedido = findById(id);
        if (request.getCliente() != null) {
            pedido.setCliente(request.getCliente());
        }
        if (request.getFecha() != null) {
            pedido.setFecha(request.getFecha());
        }
        if (request.getTotal() != null) {
            pedido.setTotal(request.getTotal());
        }
        return pedidoRepository.save(pedido);
    }

    public void delete(Long id) {
        Pedido pedido = findById(id);
        pedidoRepository.delete(pedido);
    }
}

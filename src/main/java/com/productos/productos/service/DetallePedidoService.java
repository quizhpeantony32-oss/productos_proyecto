package com.productos.productos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.productos.entity.DetallePedido;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.DetallePedidoRepository;
import com.productos.productos.repository.PedidoRepository;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;

    public DetallePedidoService(
            DetallePedidoRepository detallePedidoRepository,
            PedidoRepository pedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<DetallePedido> findByPedido(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new ResourceNotFoundException("Pedido no encontrado con id: " + pedidoId);
        }
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }
}

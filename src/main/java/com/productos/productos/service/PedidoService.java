package com.productos.productos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.productos.productos.dto.ActualizarEstadoPedidoRequest;
import com.productos.productos.dto.CrearPedidoRequest;
import com.productos.productos.dto.ItemPedidoRequest;
import com.productos.productos.entity.Cliente;
import com.productos.productos.entity.DetallePedido;
import com.productos.productos.entity.EstadoPedido;
import com.productos.productos.entity.Pedido;
import com.productos.productos.entity.Producto;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.ClienteRepository;
import com.productos.productos.repository.PedidoRepository;
import com.productos.productos.repository.ProductoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    public List<Pedido> findByCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + clienteId);
        }
        return pedidoRepository.findByClienteId(clienteId);
    }

    @Transactional
    public Pedido create(CrearPedidoRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: "
                        + request.getClienteId()));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .estado(EstadoPedido.PENDIENTE)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedidoRequest item : request.getItems()) {
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }

            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: "
                            + item.getProductoId()));

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);

            DetallePedido detalle = DetallePedido.builder()
                    .producto(producto)
                    .cantidad(item.getCantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .build();
            pedido.agregarDetalle(detalle);
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido updateEstado(Long id, ActualizarEstadoPedidoRequest request) {
        Pedido pedido = findById(id);
        pedido.setEstado(request.getEstado());
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void delete(Long id) {
        Pedido pedido = findById(id);
        pedidoRepository.delete(pedido);
    }
}

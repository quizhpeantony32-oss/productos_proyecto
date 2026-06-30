package com.productos.productos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.productos.entity.DetallePedido;
import com.productos.productos.entity.Pedido;
import com.productos.productos.entity.Producto;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.DetallePedidoRepository;
import com.productos.productos.repository.PedidoRepository;
import com.productos.productos.repository.ProductoRepository;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public DetallePedidoService(
            DetallePedidoRepository detallePedidoRepository,
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    public List<DetallePedido> findAll() {
        return detallePedidoRepository.findAll();
    }

    public DetallePedido findById(Long id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de pedido no encontrado con id: " + id));
    }

    public DetallePedido create(DetallePedido detallePedido) {
        Long pedidoId = extractPedidoId(detallePedido);
        Long productoId = extractProductoId(detallePedido);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + pedidoId));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        detallePedido.setId(null);
        detallePedido.setPedido(pedido);
        detallePedido.setProducto(producto);

        if (detallePedido.getCantidad() == null || detallePedido.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detallePedido.getPrecioUnitario() == null) {
            detallePedido.setPrecioUnitario(producto.getPrecio());
        }

        DetallePedido saved = detallePedidoRepository.save(detallePedido);
        return saved;
    }

    public DetallePedido update(Long id, DetallePedido request) {
        DetallePedido detallePedido = findById(id);

        if (request.getPedido() != null && request.getPedido().getId() != null) {
            Long pedidoId = request.getPedido().getId();
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + pedidoId));
            detallePedido.setPedido(pedido);
        }

        if (request.getProducto() != null && request.getProducto().getId() != null) {
            Long productoId = request.getProducto().getId();
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
            detallePedido.setProducto(producto);
        }

        if (request.getCantidad() != null) {
            if (request.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            detallePedido.setCantidad(request.getCantidad());
        }

        if (request.getPrecioUnitario() != null) {
            detallePedido.setPrecioUnitario(request.getPrecioUnitario());
        } else if (detallePedido.getPrecioUnitario() == null && detallePedido.getProducto() != null) {
            detallePedido.setPrecioUnitario(detallePedido.getProducto().getPrecio());
        }

        DetallePedido saved = detallePedidoRepository.save(detallePedido);
        return saved;
    }

    public void delete(Long id) {
        DetallePedido detallePedido = findById(id);
        detallePedidoRepository.delete(detallePedido);
    }

    private Long extractPedidoId(DetallePedido detallePedido) {
        if (detallePedido == null || detallePedido.getPedido() == null || detallePedido.getPedido().getId() == null) {
            throw new IllegalArgumentException("Debe enviar pedido.id en detallePedido");
        }
        return detallePedido.getPedido().getId();
    }

    private Long extractProductoId(DetallePedido detallePedido) {
        if (detallePedido == null || detallePedido.getProducto() == null || detallePedido.getProducto().getId() == null) {
            throw new IllegalArgumentException("Debe enviar producto.id en detallePedido");
        }
        return detallePedido.getProducto().getId();
    }
}

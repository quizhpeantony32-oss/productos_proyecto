package com.productos.productos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.productos.entity.DetalleVenta;
import com.productos.productos.entity.Producto;
import com.productos.productos.entity.Venta;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.DetalleVentaRepository;
import com.productos.productos.repository.ProductoRepository;
import com.productos.productos.repository.VentaRepository;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final VentaService ventaService;

    public DetalleVentaService(
            DetalleVentaRepository detalleVentaRepository,
            VentaRepository ventaRepository,
            ProductoRepository productoRepository,
            VentaService ventaService) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.ventaService = ventaService;
    }

    public List<DetalleVenta> findAll() {
        return detalleVentaRepository.findAll();
    }

    public DetalleVenta findById(Long id) {
        return detalleVentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle de venta no encontrado con id: " + id));
    }

    public DetalleVenta create(DetalleVenta detalleVenta) {
        Long ventaId = extractVentaId(detalleVenta);
        Long productoId = extractProductoId(detalleVenta);

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + ventaId));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        detalleVenta.setId(null);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);

        if (detalleVenta.getCantidad() == null || detalleVenta.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        if (detalleVenta.getPrecioUnitario() == null) {
            detalleVenta.setPrecioUnitario(producto.getPrecio());
        }

        DetalleVenta saved = detalleVentaRepository.save(detalleVenta);
        ventaService.recalculateTotal(venta.getId());
        return saved;
    }

    public DetalleVenta update(Long id, DetalleVenta request) {
        DetalleVenta detalleVenta = findById(id);

        if (request.getVenta() != null && request.getVenta().getId() != null) {
            Long ventaId = request.getVenta().getId();
            Venta venta = ventaRepository.findById(ventaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + ventaId));
            detalleVenta.setVenta(venta);
        }

        if (request.getProducto() != null && request.getProducto().getId() != null) {
            Long productoId = request.getProducto().getId();
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
            detalleVenta.setProducto(producto);
        }

        if (request.getCantidad() != null) {
            if (request.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            detalleVenta.setCantidad(request.getCantidad());
        }

        if (request.getPrecioUnitario() != null) {
            detalleVenta.setPrecioUnitario(request.getPrecioUnitario());
        } else if (detalleVenta.getPrecioUnitario() == null && detalleVenta.getProducto() != null) {
            detalleVenta.setPrecioUnitario(detalleVenta.getProducto().getPrecio());
        }

        DetalleVenta saved = detalleVentaRepository.save(detalleVenta);
        ventaService.recalculateTotal(saved.getVenta().getId());
        return saved;
    }

    public void delete(Long id) {
        DetalleVenta detalleVenta = findById(id);
        Long ventaId = detalleVenta.getVenta().getId();
        detalleVentaRepository.delete(detalleVenta);
        ventaService.recalculateTotal(ventaId);
    }

    private Long extractVentaId(DetalleVenta detalleVenta) {
        if (detalleVenta == null || detalleVenta.getVenta() == null || detalleVenta.getVenta().getId() == null) {
            throw new IllegalArgumentException("Debe enviar venta.id en detalleVenta");
        }
        return detalleVenta.getVenta().getId();
    }

    private Long extractProductoId(DetalleVenta detalleVenta) {
        if (detalleVenta == null || detalleVenta.getProducto() == null || detalleVenta.getProducto().getId() == null) {
            throw new IllegalArgumentException("Debe enviar producto.id en detalleVenta");
        }
        return detalleVenta.getProducto().getId();
    }
}

package com.productos.productos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.productos.entity.Cliente;
import com.productos.productos.entity.DetalleVenta;
import com.productos.productos.entity.Venta;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.ClienteRepository;
import com.productos.productos.repository.DetalleVentaRepository;
import com.productos.productos.repository.VentaRepository;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public VentaService(
            VentaRepository ventaRepository,
            ClienteRepository clienteRepository,
            DetalleVentaRepository detalleVentaRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public List<Venta> findAll() {
        return ventaRepository.findAll();
    }

    public Venta findById(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con id: " + id));
    }

    public Venta create(Venta venta) {
        Long clienteId = extractClienteId(venta);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + clienteId));

        venta.setId(null);
        venta.setCliente(cliente);
        if (venta.getTotal() == null) {
            venta.setTotal(BigDecimal.ZERO);
        }
        return ventaRepository.save(venta);
    }

    public Venta update(Long id, Venta request) {
        Venta venta = findById(id);

        if (request.getFecha() != null) {
            venta.setFecha(request.getFecha());
        }

        if (request.getCliente() != null && request.getCliente().getId() != null) {
            Long clienteId = request.getCliente().getId();
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + clienteId));
            venta.setCliente(cliente);
        }

        return ventaRepository.save(venta);
    }

    public void delete(Long id) {
        Venta venta = findById(id);
        ventaRepository.delete(venta);
    }

    public Venta recalculateTotal(Long ventaId) {
        Venta venta = findById(ventaId);
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);

        BigDecimal total = detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .filter(subtotal -> subtotal != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venta.setTotal(total);
        return ventaRepository.save(venta);
    }

    private Long extractClienteId(Venta venta) {
        if (venta == null || venta.getCliente() == null || venta.getCliente().getId() == null) {
            throw new IllegalArgumentException("Debe enviar cliente.id para crear o actualizar una venta");
        }
        return venta.getCliente().getId();
    }
}

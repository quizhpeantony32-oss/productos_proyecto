package com.productos.productos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.productos.productos.entity.Producto;
import com.productos.productos.exception.ResourceNotFoundException;
import com.productos.productos.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    public Producto create(Producto producto) {
        producto.setId(null);
        return productoRepository.save(producto);
    }

    public Producto update(Long id, Producto request) {
        Producto producto = findById(id);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        return productoRepository.save(producto);
    }

    public void delete(Long id) {
        Producto producto = findById(id);
        productoRepository.delete(producto);
    }
}

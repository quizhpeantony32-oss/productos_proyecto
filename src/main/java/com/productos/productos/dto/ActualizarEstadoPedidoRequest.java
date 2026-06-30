package com.productos.productos.dto;

import com.productos.productos.entity.EstadoPedido;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoPedidoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;
}

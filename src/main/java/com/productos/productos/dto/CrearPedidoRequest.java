package com.productos.productos.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPedidoRequest {

    @NotNull(message = "El clienteId es obligatorio")
    private Long clienteId;

    @Valid
    @NotEmpty(message = "El pedido debe tener al menos un item")
    private List<ItemPedidoRequest> items;
}

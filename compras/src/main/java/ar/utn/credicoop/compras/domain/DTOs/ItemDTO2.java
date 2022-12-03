package ar.utn.credicoop.compras.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO2 {
    private int cantidad;
    private ProductoPersonalizadoDTO2 producto;
    private Double precioItem;

    public ItemDTO2(int cantidad, ProductoPersonalizadoDTO2 producto, Double precioItem) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.precioItem = precioItem;
    }
}

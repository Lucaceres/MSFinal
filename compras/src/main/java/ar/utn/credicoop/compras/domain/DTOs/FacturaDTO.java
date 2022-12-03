package ar.utn.credicoop.compras.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class FacturaDTO {

    private String metodoDePago;
    private List<ItemDTO2> items;
    private RegistroCompraDTO EstadoCompra;
    private String nombreComprador;
    private String nombreVendedor;
    private Double precioTotal;


    public FacturaDTO(String metodoDePago, String nombreComprador, List<ItemDTO2> items, RegistroCompraDTO registroEstadoCompra, String nombreVendedor, Double precioTotal) {
        this.metodoDePago = metodoDePago;
        this.items= items;
        this.EstadoCompra = registroEstadoCompra;
        this.nombreComprador = nombreComprador;
        this.nombreVendedor = nombreVendedor;
        this.precioTotal = precioTotal;
    }
}

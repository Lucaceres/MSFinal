package ar.utn.credicoop.productobase.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductoBaseDTO {
    private String nombre;

    private Double precioBase;

    private String descripcion;

    private String tiempoFabricacion;

    private List<AreaPorProductoBaseDTO> posiblesPersonalizaciones;

    public ProductoBaseDTO() {}

    public ProductoBaseDTO(String nombre, Double precioBase, String descripcion, String tiempoFabricacion, List<AreaPorProductoBaseDTO> posiblesPersonalizaciones) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.descripcion = descripcion;
        this.tiempoFabricacion = tiempoFabricacion;
        this.posiblesPersonalizaciones = posiblesPersonalizaciones;
    }
}

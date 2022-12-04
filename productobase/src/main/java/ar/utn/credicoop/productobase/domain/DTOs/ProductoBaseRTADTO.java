package ar.utn.credicoop.productobase.domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProductoBaseRTADTO {
  private String nombre;
  private Double precioBase;
  private String descripcion;
  private String tiempoFabricacion;

  public ProductoBaseRTADTO(String nombre, Double precioBase, String descripcion, String tiempoFabricacion) {
    this.nombre = nombre;
    this.precioBase = precioBase;
    this.descripcion = descripcion;
    this.tiempoFabricacion = tiempoFabricacion;

  }
}

package ar.utn.credicoop.compras.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ProductoPersonalizadoDTO2 {

  private String nombreProductoBase;
  private Double precioFinal;

  public ProductoPersonalizadoDTO2(String nombreProductoBase, Double precioFinal)
  {
    this.nombreProductoBase = nombreProductoBase;
    this.precioFinal=precioFinal;
  }

}

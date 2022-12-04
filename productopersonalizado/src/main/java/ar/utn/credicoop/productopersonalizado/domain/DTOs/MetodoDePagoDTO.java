package ar.utn.credicoop.productopersonalizado.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class MetodoDePagoDTO {

  private Boolean existe;
  private Integer idUnico;
  private String nombre;

  public MetodoDePagoDTO(Boolean existe, Integer idUnico, String nombre) {
    this.existe = existe;
    this.idUnico = idUnico;
    this.nombre = nombre;
  }
}

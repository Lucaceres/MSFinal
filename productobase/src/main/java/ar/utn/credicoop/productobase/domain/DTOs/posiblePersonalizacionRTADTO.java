package ar.utn.credicoop.productobase.domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
public class posiblePersonalizacionRTADTO {
  private String lugarPersonalizable;
  List<String> TiposPersonalizacion;
}

package ar.utn.credicoop.compras.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VendedorDTO {
  private String nombre;
  private String apellido;
  private List<Integer> metodosDePagoId;
}

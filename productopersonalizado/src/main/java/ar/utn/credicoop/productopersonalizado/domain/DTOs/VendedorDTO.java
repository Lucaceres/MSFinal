package ar.utn.credicoop.productopersonalizado.domain.DTOs;

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

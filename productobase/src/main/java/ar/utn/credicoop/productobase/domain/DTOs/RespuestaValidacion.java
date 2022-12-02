package ar.utn.credicoop.productobase.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RespuestaValidacion {
    private Boolean valido;
    private String mensaje;


    public RespuestaValidacion(Boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
    }
}

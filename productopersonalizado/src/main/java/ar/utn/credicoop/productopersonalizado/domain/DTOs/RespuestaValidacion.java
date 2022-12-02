package ar.utn.credicoop.productopersonalizado.domain.DTOs;

import lombok.Getter;

@Getter
public class RespuestaValidacion {
    private Boolean valido;
    private String mensaje;

    public RespuestaValidacion(Boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
    }
}

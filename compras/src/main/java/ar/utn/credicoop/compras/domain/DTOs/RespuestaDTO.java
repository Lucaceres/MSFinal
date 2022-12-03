package ar.utn.credicoop.compras.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaDTO {
    private Mensaje RESPUESTA;
    private FacturaDTO FACTURA;

    public RespuestaDTO(Mensaje respuesta, FacturaDTO factura) {
        RESPUESTA = respuesta;
        FACTURA = factura;
    }
}

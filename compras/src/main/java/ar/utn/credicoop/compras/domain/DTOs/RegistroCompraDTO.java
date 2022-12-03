package ar.utn.credicoop.compras.domain.DTOs;

import ar.utn.credicoop.compras.domain.model.entities.EstadoCompra;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class RegistroCompraDTO {

    private EstadoCompra estadoCompra;
    private LocalDateTime fechaHoraCompra;


    public RegistroCompraDTO(LocalDateTime fechaHoraCompra, EstadoCompra estadoCompra) {

        this.fechaHoraCompra = fechaHoraCompra;
        this.estadoCompra = estadoCompra;
    }
}

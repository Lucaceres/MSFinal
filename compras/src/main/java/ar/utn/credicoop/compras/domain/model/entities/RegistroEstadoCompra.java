package ar.utn.credicoop.compras.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Registro_estado_compra")
public class RegistroEstadoCompra extends Persistente{
    @Column(name = "fecha")
    private LocalDateTime fechaHoraCompra;

    @Enumerated(EnumType.STRING)
    private EstadoCompra estadoCompra;

    @ManyToOne
    @JoinColumn(name = "compra_id")
    private Compra compra;


    public RegistroEstadoCompra() {}

    public RegistroEstadoCompra(LocalDateTime fechaHoraCompra, EstadoCompra estadoCompra, Compra compra) {
        this.fechaHoraCompra = fechaHoraCompra;
        this.estadoCompra = estadoCompra;
        this.compra = compra;
    }
}

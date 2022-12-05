package ar.utn.credicoop.productopersonalizado.domain.model.entities;

import ar.utn.credicoop.productopersonalizado.domain.GenerarCodigoUnico;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "MetodoDePago")
@Setter
@Getter
public class MetodoDePago extends Persistente {

    @Column(name = "MetodoDePago")
    private String metodoDePago;


    public MetodoDePago() {
    }

    public MetodoDePago(String metodoDePago) {
        this.metodoDePago = metodoDePago;
    }
}

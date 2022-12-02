package ar.utn.credicoop.productobase.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Area")
@Setter
@Getter
public class Area extends Persistente{
    @Column(name = "Lugar_Personalizable")
    private String lugarPersonalizable;

    public Area() {}

    public Area(String lugarPersonalizable) {
        this.lugarPersonalizable = lugarPersonalizable;
    }
}

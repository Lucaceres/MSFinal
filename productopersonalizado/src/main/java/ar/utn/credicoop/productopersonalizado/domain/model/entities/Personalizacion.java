package ar.utn.credicoop.productopersonalizado.domain.model.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Personalizacion")
@Setter
@Getter
public class Personalizacion extends Persistente {

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "Precio")
    private Double precio;

    //CAMBIE EL @ManyToOne a notnull
    //Y en lugar de dejar el tipo de dato le mande el id de una por eso el INTEGER

    @Column(name = "TipoPersonalizacion_id")
    @NotNull
    private Integer tipoPersonalizacion;

    // @OneToOne
    // @JoinColumn(name = "Area_id")
    @Column(name = "areaPersonalizable_id")
    @NotNull
    private Integer areaPersonalizable;

    public Personalizacion() {}

    public Personalizacion(String descripcion, Double precio, Integer tipoPersonalizacion, Integer areaPersonalizable) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoPersonalizacion = tipoPersonalizacion;
        this.areaPersonalizable = areaPersonalizable;
    }

}

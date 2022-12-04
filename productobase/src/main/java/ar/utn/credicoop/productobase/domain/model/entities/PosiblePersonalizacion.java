package ar.utn.credicoop.productobase.domain.model.entities;

import ar.utn.credicoop.productobase.domain.DTOs.Personalizacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "PosiblePersonalizacion")
@Getter
@Setter
public class PosiblePersonalizacion extends Persistente{

    @ManyToOne
    @JoinColumn(name = "Area_id", referencedColumnName = "id")
    private Area area;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<TipoPersonalizacion> tiposPersonalizacion;

    public PosiblePersonalizacion(){
        this.tiposPersonalizacion = new ArrayList<>();
    }

    public PosiblePersonalizacion(Area area) {
        this.area = area;
        this.tiposPersonalizacion = new ArrayList<>();
    }

    public void agregarTipoPersonalizacion(TipoPersonalizacion tipoPersonalizacion) {
        this.tiposPersonalizacion.add(tipoPersonalizacion);
    }


    public Boolean validarPersonalizacion(Personalizacion personalizacion) {
        return personalizacion.getAreaPersonalizable().equals(this.area) && this.tiposPersonalizacion.stream().anyMatch(tipoP -> tipoP.equals(personalizacion. getTipoPersonalizacion()));
    }
}

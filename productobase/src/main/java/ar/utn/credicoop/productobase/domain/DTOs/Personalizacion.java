package ar.utn.credicoop.productobase.domain.DTOs;

import ar.utn.credicoop.productobase.domain.model.entities.Area;
import ar.utn.credicoop.productobase.domain.model.entities.TipoPersonalizacion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Personalizacion {
    private String descripcion;

    private Double precio;


    private TipoPersonalizacion tipoPersonalizacion;


    private Area areaPersonalizable;

    public Personalizacion(String descripcion, Double precio, TipoPersonalizacion tipoPersonalizacion, Area areaPersonalizable) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipoPersonalizacion = tipoPersonalizacion;
        this.areaPersonalizable = areaPersonalizable;
    }
}

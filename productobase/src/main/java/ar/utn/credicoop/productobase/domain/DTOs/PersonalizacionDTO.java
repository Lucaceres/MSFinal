package ar.utn.credicoop.productobase.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalizacionDTO {
    private String descripcion;
    private double precio;
    private Integer areaId;
    private Integer tipoPersonalizacionId;

    public PersonalizacionDTO() {}

    public PersonalizacionDTO(String descripcion, double precio, Integer areaId, Integer tipoPersonalizacionId) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.areaId = areaId;
        this.tipoPersonalizacionId = tipoPersonalizacionId;
    }
}

package ar.utn.credicoop.productobase.domain.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProductoPersonalizadoDTO {
    private Integer productoBaseId;

    private List<PersonalizacionDTO> personalizacionesDTO;

    public ProductoPersonalizadoDTO(){
        this.personalizacionesDTO = new ArrayList<>();
    }

    public ProductoPersonalizadoDTO(Integer productoBaseId, List<PersonalizacionDTO> personalizacionDTO) {
        this.productoBaseId = productoBaseId;
        this.personalizacionesDTO = personalizacionDTO;
    }
}

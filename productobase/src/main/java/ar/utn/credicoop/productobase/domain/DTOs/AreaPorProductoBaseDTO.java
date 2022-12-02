package ar.utn.credicoop.productobase.domain.DTOs;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AreaPorProductoBaseDTO {
    private Integer areaId;

    @NotNull
    private List<Integer> tiposPersonalizacionId;

    public AreaPorProductoBaseDTO() {}

    public AreaPorProductoBaseDTO(Integer areaId, List<Integer> tiposPersonalizacionId) {
        this.areaId = areaId;
        this.tiposPersonalizacionId = tiposPersonalizacionId;
    }
}

package ar.utn.credicoop.productopersonalizado.domain.proxys;



import ar.utn.credicoop.productopersonalizado.domain.DTOs.ProductoPersonalizadoDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.RespuestaValidacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name="pbase")
public interface ProductoBaseProxy {
    @PostMapping("/valid")
     RespuestaValidacion validarProductoPersonalizado(@RequestBody ProductoPersonalizadoDTO producto);
}

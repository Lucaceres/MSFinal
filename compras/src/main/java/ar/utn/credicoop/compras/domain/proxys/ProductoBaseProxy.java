package ar.utn.credicoop.compras.domain.proxys;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pbase")
public interface ProductoBaseProxy {


}

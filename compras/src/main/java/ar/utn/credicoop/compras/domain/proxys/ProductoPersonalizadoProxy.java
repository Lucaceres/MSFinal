package ar.utn.credicoop.compras.domain.proxys;

import ar.utn.credicoop.compras.domain.DTOs.MetodoDePagoDTO;
import ar.utn.credicoop.compras.domain.DTOs.VendedorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ppersonalizado")
public interface ProductoPersonalizadoProxy {

  @PostMapping("/validarPP")
  Boolean existeElProducto(@RequestBody Integer productoPersonalizadoId);

  @PostMapping("/validarMP")
  MetodoDePagoDTO existeElMetodoDePago(@RequestBody Integer metodoDePagoId);

  @PostMapping("/vendedor")
  VendedorDTO traerVendedor(@RequestBody Integer vendedorId);

  @PostMapping("/vendedorId")
  Integer traerVendedorId(@RequestBody Integer productoPersonalizadoId);

  @PostMapping("/precio")
  Double calcularPrecioTotal(@RequestBody Integer productopersonalizadoId);


  @PostMapping("/nombre")
  String buscarProductoBase(@RequestBody Integer productoBaseId);

  @PostMapping("/publicado")
  Boolean validarPublicado(@RequestBody Integer productoPersonalizado);

  @PostMapping("/metodopago")
   String buscarNomrbeMetodoPago(@RequestBody Integer metodoPago);

}
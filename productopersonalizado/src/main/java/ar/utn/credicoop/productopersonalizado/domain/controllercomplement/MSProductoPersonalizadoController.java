package ar.utn.credicoop.productopersonalizado.domain.controllercomplement;

import ar.utn.credicoop.productopersonalizado.domain.DTOs.*;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.MetodoDePago;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Personalizacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.ProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Vendedor;
import ar.utn.credicoop.productopersonalizado.domain.proxys.ProductoBaseProxy;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoMetodoDePago;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoVendedor;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
public class MSProductoPersonalizadoController {

    @Autowired
    RepoProductoPersonalizado repoProductoPersonalizado;

    @Autowired
    RepoVendedor repoVendedor;

    @Autowired
    RepoMetodoDePago repoMetodoDePago;

    @Autowired
    ProductoBaseProxy productoBaseProxy;

    @Retry(name = "ppersonalizado")
    @Transactional
    @PostMapping("productospersonalizados/{vendedorId}")
    public @ResponseBody ResponseEntity<Object> crearProductoPersonalizado(@PathVariable("vendedorId") Integer vendedorID, @RequestBody ProductoPersonalizadoDTO productoPersonalizadoDTO) {

        //Consigo la validacion desde el MS producto base
        RespuestaValidacion respuesta = productoBaseProxy.validarProductoPersonalizado(productoPersonalizadoDTO);

        //Obtengo la lista de pesonalizaciones del DTO
        List<PersonalizacionDTO> personalizacionesDTOList = productoPersonalizadoDTO.getPersonalizacionesDTO();
        //Obtengo al vendedor
        Optional<Vendedor> vendedorOptional= repoVendedor.findById(vendedorID);
        //Busco al vendedor
        if(vendedorOptional.isPresent()) {
            if (respuesta.getValido()) {
                //Creo lista de personalizaciones con la lista de persoDTO para despues ponerlo en el constructor
                List<Personalizacion> personalizacionList = personalizacionesDTOList.stream().map(personalizacionDTO -> new Personalizacion(personalizacionDTO.getDescripcion(), personalizacionDTO.getPrecio(), personalizacionDTO.getTipoPersonalizacionId(), personalizacionDTO.getAreaId())).collect(Collectors.toList());
                //Creo el producto
                ProductoPersonalizado productoPersonalizado = new ProductoPersonalizado(productoPersonalizadoDTO.getProductoBaseId(), vendedorOptional.get(), personalizacionList);
                //Lo guardo
                repoProductoPersonalizado.save(productoPersonalizado);
                //Agrego el producto a la lista de productos que tiene el vendedor
                vendedorOptional.get().agregarProducto(productoPersonalizado);
                return new ResponseEntity<Object>(respuesta.getMensaje(), HttpStatus.CREATED);
            }
            return new ResponseEntity<Object>(respuesta.getMensaje(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("Vendedor no encontrado", HttpStatus.BAD_REQUEST);
    }


    @Transactional
    @PostMapping("/validarPP")
    public Boolean existeElProducto(@RequestBody Integer productoPersonalizadoId) {

        Optional<ProductoPersonalizado> productoPersonalizadoOptional = repoProductoPersonalizado.findById(productoPersonalizadoId);
        //TODO despues habria que cambiar esto para que te devuelva un codigo que no sea el id para guardar en la otra base de datos
        return productoPersonalizadoOptional.isPresent();
    }

    @Transactional
    @PostMapping("/validarMP") public MetodoDePagoDTO existeElMetodoDePago(@RequestBody Integer metodoDePagoId) {

        Optional<MetodoDePago> metodoDePagoOptional = repoMetodoDePago.findById(metodoDePagoId);

        if (metodoDePagoOptional.isPresent()) {
            MetodoDePago metodoDePagoElegido = metodoDePagoOptional.get();
            return new MetodoDePagoDTO(true,metodoDePagoElegido.getId(),metodoDePagoElegido.getMetodoDePago());
        }

        return new MetodoDePagoDTO(false,null,null);
    }

    @Transactional
    @PostMapping("/vendedor")
    VendedorDTO traerVendedor(@RequestBody Integer vendedorId) {

        Optional<Vendedor> vendedor = repoVendedor.findById(vendedorId);

        // NO SE HACE VALIDACION DE SI ESTÁ PRESENTE EL VENDEDOR PORQUE SE VERIFICA EN OTRO LADO.

        List<Integer> metodosDePagolist = vendedor.get().getMetodosDePagos().stream().map(MetodoDePago::getId).collect(Collectors.toList());
        return new VendedorDTO(vendedor.get().getNombre(),vendedor.get().getApellido(),metodosDePagolist);

    }

    @Transactional
    @PostMapping("/precio")
    Double calcularPrecioTotal(@RequestBody Integer productopersonalizadoId)
    {
        //BUSCO EL PPERSONALIZADO
         Optional<ProductoPersonalizado> productoPersonalizadoOptional = repoProductoPersonalizado.findById(productopersonalizadoId);
         //BUSCO EL PRODUCTO BASE ID
         Integer productoBaseId = productoPersonalizadoOptional.get().getProductoBase();
         //BUSCO EL PRECIO BASE DEL PBASE CON EL PBASE ID
         Double precioBase= productoBaseProxy.buscarPrecioBase(productoBaseId);
        //
         return productoPersonalizadoOptional.get().calcularPrecioFinal(precioBase);

    }

    @Transactional
    @PostMapping("/nombre")
    String buscarProductoBase(@RequestBody Integer productoPersonalizadoId)
    {
        Optional<ProductoPersonalizado> productoPersonalizadoOptional = repoProductoPersonalizado.findById(productoPersonalizadoId);

        //NO VERIFICAMOS QUE EXISTA PORQUE YA ESTÁ VERIFICADO ANTERIORMENTE EN EL PARAMETRO
        return productoBaseProxy.buscarNombreProductoBase(productoPersonalizadoOptional.get().getProductoBase());

    }

    @PostMapping("/publicado")
    Boolean validarPublicado(@RequestBody Integer productoPersonalizado)
    {
        return repoProductoPersonalizado.findById(productoPersonalizado).get().getEstaPublicado();
    }

    @PostMapping("/vendedorId")
    Integer traerVendedorId(@RequestBody Integer productoPersonalizadoId)
    {
        return repoProductoPersonalizado.findById(productoPersonalizadoId).get().getVendedor().getId();
    }

    @PostMapping("/metodopago")
    String buscarNomrbeMetodoPago(@RequestBody Integer metodoPago)
    {
        return repoMetodoDePago.findById(metodoPago).get().getMetodoDePago();
    }

}

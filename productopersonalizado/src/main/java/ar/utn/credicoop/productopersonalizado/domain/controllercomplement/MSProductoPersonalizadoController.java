package ar.utn.credicoop.productopersonalizado.domain.controllercomplement;

import ar.utn.credicoop.productopersonalizado.domain.DTOs.PersonalizacionDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.ProductoPersonalizadoDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.RespuestaValidacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Personalizacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.ProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Vendedor;
import ar.utn.credicoop.productopersonalizado.domain.proxys.ProductoBaseProxy;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoVendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MSProductoPersonalizadoController {

    @Autowired
    RepoProductoPersonalizado repoProductoPersonalizado;

    @Autowired
    RepoVendedor repoVendedor;

    @Autowired
    ProductoBaseProxy productoBaseProxy;

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
                vendedorOptional.get().agregarProducto(productoPersonalizado);
                return new ResponseEntity<Object>(respuesta.getMensaje(), HttpStatus.CREATED);
            }
            return new ResponseEntity<Object>(respuesta.getMensaje(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("Vendedor no encontrado", HttpStatus.BAD_REQUEST);
    }



}

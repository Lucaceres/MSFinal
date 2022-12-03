package ar.utn.credicoop.productopersonalizado.domain.controllercomplement;

import ar.utn.credicoop.productopersonalizado.domain.DTOs.PersonalizacionDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.ProductoPersonalizadoDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.RespuestaValidacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Personalizacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.ProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Vendedor;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion.Publicacion;
import ar.utn.credicoop.productopersonalizado.domain.proxys.ProductoBaseProxy;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoPublicacion;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoVendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RepositoryRestController
public class ProductoPersonalizadoController {

    @Autowired
    RepoProductoPersonalizado repoProductoPersonalizado;

    @Autowired
    RepoVendedor repoVendedor;

    @Autowired
    RepoPublicacion repoPublicacion;

    @Transactional
    @DeleteMapping("/productopersonalizado/{id}")
    public @ResponseBody ResponseEntity<Object> delete(@PathVariable("id") Integer id) {

        Optional<ProductoPersonalizado> productoPersonalizadoOptional = repoProductoPersonalizado.findById(id);

        //VALIDACIONES

        if(productoPersonalizadoOptional.isPresent()) {
            //valido si esta publicado y si esta publicado borro la publicacion
            if(productoPersonalizadoOptional.get().getEstaPublicado()){

                Vendedor vendedor = productoPersonalizadoOptional.get().getVendedor();
                Publicacion publicacion = vendedor.getTienda().getPublicaciones().stream().filter(publicacion2 -> publicacion2.getProductoPublicado().equals(productoPersonalizadoOptional.get())).collect(Collectors.toList()).get(0);

                repoPublicacion.deleteById(publicacion.getId());

            }


            ProductoPersonalizado producto = productoPersonalizadoOptional.get();
            // BORRO EL PRODUCTO DE LA LISTA DEL VENDEDOR
            ProductoPersonalizado productoABorrar = productoPersonalizadoOptional.get().getVendedor().getProductosPersonalizados().stream().filter(p -> p.equals(productoPersonalizadoOptional.get())).collect(Collectors.toList()).get(0);
            repoProductoPersonalizado.deleteById(id);


            return ResponseEntity.ok("producto personalizado borrado");
        }
        return new ResponseEntity<Object>("El producto no existe", HttpStatus.NOT_FOUND);

    }

}

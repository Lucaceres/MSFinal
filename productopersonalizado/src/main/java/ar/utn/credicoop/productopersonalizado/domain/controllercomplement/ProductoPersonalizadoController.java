package ar.utn.credicoop.productopersonalizado.domain.controllercomplement;

import ar.utn.credicoop.productopersonalizado.domain.DTOs.PersonalizacionDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.ProductoPersonalizadoDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.RespuestaValidacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Personalizacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.ProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.proxys.ProductoBaseProxy;
import ar.utn.credicoop.productopersonalizado.domain.repositories.RepoProductoPersonalizado;
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

    @Transactional
    @DeleteMapping("/productopersonalizado/{id}")
    public @ResponseBody ResponseEntity<Object> delete(@PathVariable("id") Integer id) {

        Optional<ProductoPersonalizado> productoPersonalizadoOptional = repoProductoPersonalizado.findById(id);

        //VALIDACIONES

        if(productoPersonalizadoOptional.isPresent()) {
            ProductoPersonalizado producto = productoPersonalizadoOptional.get();
            repoProductoPersonalizado.deleteById(id);

            return ResponseEntity.ok("producto personalizado borrado");
        }
        return new ResponseEntity<Object>("El producto no existe", HttpStatus.NOT_FOUND);

    }



}

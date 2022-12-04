package ar.utn.credicoop.compras.domain.controllercomplement;

import ar.utn.credicoop.compras.domain.model.entities.*;
import ar.utn.credicoop.compras.domain.repositories.RepoCompra;
import ar.utn.credicoop.compras.domain.repositories.RepoComprador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RepositoryRestController(path = "compra")
public class compradorController {

    @Autowired
    RepoComprador repoComprador;

    @Autowired
    RepoCompra repoCompra;

    @Autowired
    RepoRegistroCompra repoRegistroCompra;


    @Transactional
    @DeleteMapping("/compradores/{compradorID}/carritodecompra/items")
    public @ResponseBody
    ResponseEntity<Object> borrarItem(@PathVariable("compradorID") Integer compradorID,
                                      @RequestBody Integer itemID) throws Exception {

        Optional<Comprador> comprador = repoComprador.findById(compradorID);

        if (comprador.isPresent()) {

            comprador.get().getCarritoActual().eliminarProducto(itemID);

            return new ResponseEntity<>("Item borrado con exito", HttpStatus.OK);

        }
        return new ResponseEntity<>("El comprador no existe", HttpStatus.NOT_FOUND);
    }

    //TODO probar este metodo de mostrar items para ver si queda bien
    @Transactional
    @GetMapping("/compradores/{compradorID}/carritodecompra")
    public @ResponseBody
    ResponseEntity<Object> mostrarItems(@PathVariable("compradorID") Integer compradorID) {
        Optional<Comprador> comprador = repoComprador.findById(compradorID);

        if (comprador.isPresent()) {
            return new ResponseEntity<>(comprador.get().getCarritoActual(), HttpStatus.OK);
        }

        return new ResponseEntity<>("El comprador no existe", HttpStatus.NOT_FOUND);

    }

}
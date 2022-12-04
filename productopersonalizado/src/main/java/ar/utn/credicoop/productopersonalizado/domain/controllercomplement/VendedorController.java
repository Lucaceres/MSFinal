package ar.utn.credicoop.productopersonalizado.domain.controllercomplement;

import ar.utn.credicoop.productopersonalizado.domain.DTOs.PublicacionDTO;
import ar.utn.credicoop.productopersonalizado.domain.DTOs.VendedorDTO;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.MetodoDePago;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.ProductoPersonalizado;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.Vendedor;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion.EstadoPublicacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion.Publicacion;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion.RegistroEstadoPublicacion;
import ar.utn.credicoop.productopersonalizado.domain.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RepositoryRestController
public class VendedorController {
    @Autowired
    RepoMetodoDePago repoMetodoDePago;

    @Autowired
    RepoVendedor repoVendedor;

   // @Autowired
   // RepoCompra repoCompra;

   // @Autowired
   // RepoRegistroCompra repoRegistroCompra;

    @Autowired
    RepoProductoPersonalizado repoProductoPersonalizado;

    @Autowired
    RepoPublicacion repoPublicacion;

    @Autowired
    RepoRegistroPublicacion repoRegistroPublicacion;
    @Transactional
    @PostMapping("/vendedores")
    public @ResponseBody ResponseEntity<Object> crearVendedor(@RequestBody VendedorDTO vendedorDTO) {


        List<Integer> listaDeMetodosDePagoIDs = vendedorDTO.getMetodosDePagoId();
        //OBTENEMOS TODOS LOS METODOS DE PAGO
        List<Optional<MetodoDePago>> listaDeMetodosDePagoOptional = listaDeMetodosDePagoIDs.stream().map(pago -> repoMetodoDePago.findById(pago)).collect(Collectors.toList());

        //VALIDAMOS LOS METODOS DE PAGO
        if (listaDeMetodosDePagoOptional.stream().allMatch(metodo -> metodo.isPresent())) {
            List<MetodoDePago> listaDeMetodosDePago = listaDeMetodosDePagoOptional.stream().map(pago -> pago.get()).collect(Collectors.toList());


            Vendedor nuevoVendedor = new Vendedor(vendedorDTO.getNombre(), vendedorDTO.getApellido(), listaDeMetodosDePago);
            repoVendedor.save(nuevoVendedor);

            return new ResponseEntity<Object>("El vendedor fue creado con éxito", HttpStatus.CREATED);
        }
        return new ResponseEntity<Object>("Alguno de los métodos de pago no existe", HttpStatus.NOT_FOUND);
    }



    @Transactional
    @PostMapping("/vendedores/{vendedorID}/publicaciones")
    public @ResponseBody ResponseEntity<Object> subirPublicacion(@PathVariable("vendedorID") Integer vendedorID,
                                                                 @RequestBody PublicacionDTO publicacionDTO) {

        Integer id = publicacionDTO.getProductoPersonalizadoId();
        Optional<ProductoPersonalizado> productoPersonalizadoAPublicar = repoProductoPersonalizado.findById(id);
        Optional<Vendedor> vendedor = repoVendedor.findById(vendedorID);

        if (productoPersonalizadoAPublicar.isPresent()) {

            if (vendedor.isPresent()) {

                if(vendedor.get().tieneElProducto(productoPersonalizadoAPublicar.get())) {

                    productoPersonalizadoAPublicar.get().setEstaPublicado(true);
                    Publicacion publicacion = new Publicacion(publicacionDTO.getNombre(),productoPersonalizadoAPublicar.get());
                    LocalDateTime fechaHora = LocalDateTime.now();
                    publicacion.agregarRegistro(new RegistroEstadoPublicacion(fechaHora, EstadoPublicacion.ACTIVA,publicacion));
                    vendedor.get().getTienda().agregarPublicacion(publicacion);
                    repoPublicacion.save(publicacion);


                    return new ResponseEntity<Object>("El producto fue publicado con éxito", HttpStatus.ACCEPTED);

                }
                return new ResponseEntity<Object>("NO TIENE EL PRODUCTO ESCOGIDO", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Object>("El VENDEDOR NO FUE ENCONTRADO", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>("El PRODUCTO PERSONALIZADO NO FUE ENCONTRADO", HttpStatus.NOT_FOUND);
    }
    @Transactional
    @DeleteMapping("/vendedores/{id}")
    public @ResponseBody ResponseEntity<Object> delete(@PathVariable("id") Integer id) {

        Optional<Vendedor> vendedorOptional = repoVendedor.findById(id);

        //VALIDACIONES

        if(vendedorOptional.isPresent()) {
            Vendedor vendedor = vendedorOptional.get();
            repoProductoPersonalizado.deleteById(id);

            return ResponseEntity.ok("Vendedor borrado");
        }
        return new ResponseEntity<Object>("El vendedor no existe", HttpStatus.NOT_FOUND);

    }

    @Transactional
    @PostMapping("/vendedores/{vendedorId}/publicaciones/{publicacionId}/pausar")
    public @ResponseBody ResponseEntity<Object> pausarPublicacion(@PathVariable("vendedorId") Integer vendedorId,
                                                                  @PathVariable("publicacionId") Integer publicacionId) {

        //BUSCAMOS LA PUBLICACION
        Optional<Publicacion> publicacion = repoPublicacion.findById(publicacionId);

        if (publicacion.isPresent()) {

            //CAMBIAMOS EL ESTADO DE LA PUBLICACION

            LocalDateTime fechaHora = LocalDateTime.now();

            RegistroEstadoPublicacion nuevoRegistro = new RegistroEstadoPublicacion(fechaHora, EstadoPublicacion.PAUSADA, publicacion.get());
            repoRegistroPublicacion.save(nuevoRegistro);

            publicacion.get().getRegistrosEstados().add(nuevoRegistro);
            repoPublicacion.save(publicacion.get());


            return new ResponseEntity<>("La publicación fue pausada con éxito!" ,HttpStatus.ACCEPTED);

        }

        return new ResponseEntity<Object>("LA PUBLICACION NO FUE ENCONTRADA", HttpStatus.NOT_FOUND);

    }

    @Transactional
    @PostMapping("/vendedores/{vendedorId}/publicaciones/{publicacionId}/cancelar")
    public @ResponseBody ResponseEntity<Object> cancelarPublicacion(@PathVariable("vendedorId") Integer vendedorId,
                                                                    @PathVariable("publicacionId") Integer publicacionId) {

        //BUSCAMOS LA PUBLICACION

        Optional<Publicacion> publicacion = repoPublicacion.findById(publicacionId);

        if (publicacion.isPresent()) {

            //CAMBIAMOS EL ESTADO DE LA PUBLICACION

            LocalDateTime fechaHora = LocalDateTime.now();

            RegistroEstadoPublicacion nuevoRegistro = new RegistroEstadoPublicacion(fechaHora, EstadoPublicacion.CANCELADA, publicacion.get());
            repoRegistroPublicacion.save(nuevoRegistro);

            publicacion.get().getRegistrosEstados().add(nuevoRegistro);
            repoPublicacion.save(publicacion.get());


            return new ResponseEntity<>("La publicación fue cancelada con éxito!" ,HttpStatus.ACCEPTED);

        }

        return new ResponseEntity<Object>("LA PUBLICACION NO FUE ENCONTRADA", HttpStatus.NOT_FOUND);

    }
    @Transactional
    @PostMapping("/vendedores/{vendedorId}/publicaciones/{publicacionId}/activar")
    public @ResponseBody ResponseEntity<Object> activarPublicacion(@PathVariable("vendedorId") Integer vendedorId,
                                                                  @PathVariable("publicacionId") Integer publicacionId) {

        //BUSCAMOS LA PUBLICACION
        Optional<Publicacion> publicacion = repoPublicacion.findById(publicacionId);

        if (publicacion.isPresent()) {

            //CAMBIAMOS EL ESTADO DE LA PUBLICACION

            LocalDateTime fechaHora = LocalDateTime.now();

            RegistroEstadoPublicacion nuevoRegistro = new RegistroEstadoPublicacion(fechaHora, EstadoPublicacion.ACTIVA, publicacion.get());
            repoRegistroPublicacion.save(nuevoRegistro);

            publicacion.get().getRegistrosEstados().add(nuevoRegistro);
            repoPublicacion.save(publicacion.get());


            return new ResponseEntity<>("La publicación fue activada con éxito!" ,HttpStatus.ACCEPTED);

        }

        return new ResponseEntity<Object>("LA PUBLICACION NO FUE ENCONTRADA", HttpStatus.NOT_FOUND);

    }

}
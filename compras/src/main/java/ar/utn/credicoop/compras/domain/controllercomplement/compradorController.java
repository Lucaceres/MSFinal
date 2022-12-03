package ar.utn.credicoop.compras.domain.controllercomplement;

import ar.utn.credicoop.compras.domain.DTOs.Mensaje;
import ar.utn.credicoop.compras.domain.model.entities.*;
import ar.utn.credicoop.compras.domain.repositories.RepoCompra;
import ar.utn.credicoop.compras.domain.repositories.RepoComprador;
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

import static ar.utn.credicoop.compras.domain.model.entities.EstadoCompra.PENDIENTE;

@RepositoryRestController(path = "compra")
public class compradorController {
    @Autowired
    RepoComprador repoComprador;


    @Autowired
    RepoCompra repoCompra;
    //TODO RESOLVER COMO OBTENER METODO DE PAGO Y PRODUCTO PERSO CON PROXYS
    @Transactional
    @PostMapping("/compradores/{compradorID}/carritodecompra/items")
    public @ResponseBody ResponseEntity<Object> agregarItem(@PathVariable("compradorID") Integer compradorID,
                                                            @RequestBody ItemDTO itemDTO) throws Exception {

        Optional<Comprador> comprador = repoComprador.findById(compradorID);
        Optional<ProductoPersonalizado> productoPersonalizado = repoProductoPersonalizado.findById(itemDTO.getProductoPersonalizadoID());

        if (comprador.isPresent() && productoPersonalizado.isPresent()) {

            Item itemAAgregar = new Item(itemDTO.getCantidad(), productoPersonalizado.get());
            comprador.get().getCarritoActual().agregarProducto(itemAAgregar);

            return new ResponseEntity<>("Item agregado con exito", HttpStatus.ACCEPTED);

        }

        return new ResponseEntity<>("El comprador o el producto elegido no existe", HttpStatus.NOT_FOUND);

    }

    @Transactional
    @DeleteMapping("/compradores/{compradorID}/carritodecompra/items")
    public @ResponseBody ResponseEntity<Object> borrarItem(@PathVariable ("compradorID") Integer compradorID,
                                                           @RequestBody Integer itemID) throws Exception {

        Optional<Comprador> comprador = repoComprador.findById(compradorID);

        if (comprador.isPresent()) {

            comprador.get().getCarritoActual().eliminarProducto(itemID);

            return new ResponseEntity<>("Item borrado con exito", HttpStatus.OK);

        }
        return new ResponseEntity<>("El comprador no existe", HttpStatus.NOT_FOUND);
    }


    @Transactional
    @GetMapping("/compradores/{compradorID}/carritodecompra")
    public @ResponseBody ResponseEntity<Object> mostrarItems(@PathVariable("compradorID") Integer compradorID)
    {
        Optional<Comprador> comprador = repoComprador.findById(compradorID);
        if(comprador.isPresent()) {
            return new ResponseEntity<>(comprador.get().getCarritoActual(), HttpStatus.OK);
        }

        return new ResponseEntity<>("El comprador no existe", HttpStatus.NOT_FOUND);

    }


    @Transactional
    @PostMapping("/compradores/{compradorID}/compra")
    public @ResponseBody ResponseEntity<Object> comprar(@PathVariable("compradorID") Integer compradorID, @RequestBody CompraDTO compraDTO) throws Exception {

        Optional<Comprador> comprador = repoComprador.findById(compradorID);
        Optional<MetodoDePago> metodoDePagoElegido = repoMetodoDePago.findById(compraDTO.getMetodoDePagoId());


        //VALIDO SI EL COMPRADOR EXISTE
        if(comprador.isPresent()) {

            //VALIDO SI EXISTE EL METODO DE PAGO
            if(metodoDePagoElegido.isPresent()) {

                Vendedor vendedorElegido = comprador.get().getCarritoActual().vendedorElegido();

                //VALIDO SI EL VENDEDOR ACEPTA EL METODO DE PAGO SELECCIONADO
                if(vendedorElegido.getMetodosDePagos().contains(metodoDePagoElegido.get())) {

                    LocalDateTime fechaDeHoy = LocalDateTime.now();

                    Compra nuevaCompra = new Compra(metodoDePagoElegido.get(),comprador.get().getCarritoActual(),comprador.get(),vendedorElegido);

                    nuevaCompra.getRegistroEstadosCompra().add(new RegistroEstadoCompra(fechaDeHoy, PENDIENTE,nuevaCompra));

                    repoCompra.save(nuevaCompra);

                    // REALIZAMOS LA FACTURA

                    List<ItemDTO2> itemsCarrito = comprador.get().getCarritoActual().getItemsAComprar().stream().map(item -> new ItemDTO2(item.getCantidad(),new ProductoPersonalizadoDTO2(item.getProductoPersonalizado().getProductoBase().getNombre(),item.getProductoPersonalizado().calcularPrecioFinal()),item.calcularPrecio())).collect(Collectors.toList());


                    Double precioTotal = comprador.get().getCarritoActual().calcularTotal();

                    FacturaDTO factura = new FacturaDTO(metodoDePagoElegido.get().getMetodoDePago(),
                            comprador.get().getNombre(),itemsCarrito,new RegistroCompraDTO(fechaDeHoy, PENDIENTE),vendedorElegido.getNombre(),precioTotal);

                    comprador.get().getCarritosDeCompra().add(new CarritoDecompra());


                    return new ResponseEntity<>(new RespuestaDT(new Mensaje("Compra realizada con éxito"),factura), HttpStatus.CREATED);

                }
                return new ResponseEntity<>("EL VENDEDOR NO ACEPTA EL METODO DE PAGO", HttpStatus.BAD_REQUEST);

            }

            return new ResponseEntity<>("EL MÉTODO DE PAGO NO EXISTE", HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<>("EL COMPRADOR INGRESADO NO EXISTE", HttpStatus.NOT_FOUND);

    }
}

package ar.utn.credicoop.compras.domain.controllercomplement;

import ar.utn.credicoop.compras.domain.DTOs.*;
import ar.utn.credicoop.compras.domain.model.entities.*;
import ar.utn.credicoop.compras.domain.proxys.ProductoPersonalizadoProxy;
import ar.utn.credicoop.compras.domain.repositories.RepoCompra;
import ar.utn.credicoop.compras.domain.repositories.RepoComprador;
import ar.utn.credicoop.compras.domain.repositories.RepoRegistroCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.utn.credicoop.compras.domain.model.entities.EstadoCompra.PENDIENTE;

@RestController
public class MScompradorController {

  @Autowired
  RepoComprador repoComprador;

  @Autowired
  RepoCompra repoCompra;

  @Autowired
  RepoRegistroCompra repoRegistroCompra;

  @Autowired
  ProductoPersonalizadoProxy productoPersonalizadoProxy;


  @Transactional
  @PostMapping("/compradores/{compradorID}/carritodecompra/items")
  public @ResponseBody
  ResponseEntity<Object> agregarItem(@PathVariable("compradorID") Integer compradorID,
                                     @RequestBody ItemDTO itemDTO) throws Exception {

    Optional<Comprador> comprador = repoComprador.findById(compradorID);

    if (productoPersonalizadoProxy.existeElProducto(itemDTO.getProductoPersonalizadoID())) {

      if (comprador.isPresent()) {
        // VERIFICA QUE EL PRODUCTO ESTE PUBLICADO
        if (productoPersonalizadoProxy.validarPublicado(itemDTO.getProductoPersonalizadoID())) {
          if (comprador.get().getCarritoActual().getItemsAComprar().isEmpty()) {
            Item itemAAgregar = new Item(itemDTO.getCantidad(), itemDTO.getProductoPersonalizadoID(), productoPersonalizadoProxy.calcularPrecioTotal(itemDTO.getProductoPersonalizadoID()));
            comprador.get().getCarritoActual().agregarProducto(itemAAgregar);

            return new ResponseEntity<>("Item agregado con exito", HttpStatus.ACCEPTED);
          }
          //VERIFICA QUE SEA EL MISMO VENDEDOR
          else if (productoPersonalizadoProxy.traerVendedorId(comprador.get().getCarritoActual().ultimoItem().getProductoPersonalizado()).equals(productoPersonalizadoProxy.traerVendedorId(itemDTO.getProductoPersonalizadoID()))) {
            // En el item guardo la cantidad y un id al producto personalizado
            Item itemAAgregar = new Item(itemDTO.getCantidad(), itemDTO.getProductoPersonalizadoID(), productoPersonalizadoProxy.calcularPrecioTotal(itemDTO.getProductoPersonalizadoID()));
            comprador.get().getCarritoActual().agregarProducto(itemAAgregar);

            //TODO quizas por cada item que agregues se pueda devolver el carrito con el precio actualizado

            return new ResponseEntity<>("Item agregado con exito", HttpStatus.ACCEPTED);
          }
          return new ResponseEntity<>("Todos los item del carrito deben ser del mismo vendedor", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Producto Personalizado NO Publicado", HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>("El comprador no existe", HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("El producto personalizado no existe", HttpStatus.BAD_REQUEST);
  }


  @Transactional
  @PostMapping("/compradores/{compradorID}/compra")
  public @ResponseBody
  ResponseEntity<Object> comprar(@PathVariable("compradorID") Integer compradorID, @RequestBody CompraDTO compraDTO) throws Exception {

    Optional<Comprador> comprador = repoComprador.findById(compradorID);

    MetodoDePagoDTO metodoDePagoDTO = productoPersonalizadoProxy.existeElMetodoDePago(compraDTO.getMetodoDePagoId());


    //VALIDO SI EL COMPRADOR EXISTE
    if (comprador.isPresent()) {

      //VALIDO SI EXISTE EL METODO DE PAGO
      if (metodoDePagoDTO.getExiste()) {

        Integer vendedorElegidoId = comprador.get().getCarritoActual().vendedorElegidoId();

        VendedorDTO vendedorElegido = productoPersonalizadoProxy.traerVendedor(vendedorElegidoId);


        //VALIDO SI EL VENDEDOR ACEPTA EL METODO DE PAGO SELECCIONADO
        if (vendedorElegido.getMetodosDePagoId().contains(metodoDePagoDTO.getIdUnico())) {

          LocalDateTime fechaDeHoy = LocalDateTime.now();

          Compra nuevaCompra = new Compra(metodoDePagoDTO.getIdUnico(), comprador.get().getCarritoActual(), comprador.get(), vendedorElegidoId);

          nuevaCompra.getRegistroEstadosCompra().add(new RegistroEstadoCompra(fechaDeHoy, PENDIENTE, nuevaCompra));

          repoCompra.save(nuevaCompra);

          // REALIZAMOS LA FACTURA

          List<ItemDTO2> itemsCarrito = comprador.get().getCarritoActual().getItemsAComprar().stream().map(item -> new ItemDTO2(item.getCantidad()
              , new ProductoPersonalizadoDTO2(productoPersonalizadoProxy.buscarProductoBase(item.getProductoPersonalizado()), productoPersonalizadoProxy.calcularPrecioTotal(item.getProductoPersonalizado()))
              , item.calcularPrecio(productoPersonalizadoProxy.calcularPrecioTotal(item.getProductoPersonalizado())))).collect(Collectors.toList());


          comprador.get().getCarritoActual().getItemsAComprar().stream().forEach(item -> item.setPrecioItem(item.calcularPrecio(productoPersonalizadoProxy.calcularPrecioTotal(item.getProductoPersonalizado()))));

          Double precioTotal = comprador.get().getCarritoActual().getItemsAComprar().stream().mapToDouble(item -> item.getPrecioItem() * item.getCantidad()).sum();

          Double precioDeUnItem = comprador.get().getCarritoActual().getItemsAComprar().get(0).getPrecioItem();

          FacturaDTO factura = new FacturaDTO(metodoDePagoDTO.getNombre(),
              comprador.get().getNombre(), itemsCarrito, new RegistroCompraDTO(fechaDeHoy, PENDIENTE), vendedorElegido.getNombre(), 300.0);

          comprador.get().getCarritosDeCompra().add(new CarritoDecompra());


          return new ResponseEntity<>(new RespuestaDTO(new Mensaje("Compra realizada con éxito"), factura), HttpStatus.CREATED);

        }
        return new ResponseEntity<>("EL VENDEDOR NO ACEPTA EL METODO DE PAGO", HttpStatus.BAD_REQUEST);

      }

      return new ResponseEntity<>("EL MÉTODO DE PAGO NO EXISTE", HttpStatus.NOT_FOUND);

    }

    return new ResponseEntity<>("EL COMPRADOR INGRESADO NO EXISTE", HttpStatus.NOT_FOUND);

  }

  @Transactional
  @PostMapping("/vendedores/{compraID}/confirmar")
  public @ResponseBody ResponseEntity<Object> confirmarCompra(@PathVariable("compraID") Integer compraID) {

    //BUSCAMOS LA COMPRA
    Optional<Compra> compra = repoCompra.findById(compraID);

    if (compra.isPresent()) {

      //CAMBIAMOS EL ESTADO DE LA COMPRA

      LocalDateTime fechaHora = LocalDateTime.now();

      RegistroEstadoCompra nuevoRegistro = new RegistroEstadoCompra(fechaHora, EstadoCompra.CONFIRMADA, compra.get());
      repoRegistroCompra.save(nuevoRegistro);

      compra.get().getRegistroEstadosCompra().add(nuevoRegistro);
      repoCompra.save(compra.get());

      // REALIZAMOS LA FACTURA

      List<ItemDTO2> itemsCarrito = compra.get().getCarritoDeCompra().getItemsAComprar().stream().map(item -> new ItemDTO2(item.getCantidad(),new ProductoPersonalizadoDTO2(productoPersonalizadoProxy.buscarProductoBase(item.getProductoPersonalizado()), productoPersonalizadoProxy.calcularPrecioTotal(item.getProductoPersonalizado())),item.calcularPrecio(productoPersonalizadoProxy.calcularPrecioTotal(item.getProductoPersonalizado())))).collect(Collectors.toList());

      Double precioTotal = compra.get().getCarritoDeCompra().calcularTotal();

      FacturaDTO factura = new FacturaDTO(productoPersonalizadoProxy.buscarNomrbeMetodoPago(compra.get().getMetodoDePago()),
          compra.get().getComprador().getNombre(),itemsCarrito,new RegistroCompraDTO(fechaHora, nuevoRegistro.getEstadoCompra()),productoPersonalizadoProxy.traerVendedor(compra.get().getVendedor()).getNombre(),precioTotal);

      return new ResponseEntity<>(new RespuestaDTO(new Mensaje("La venta fue confirmada con éxito"),factura), HttpStatus.CREATED);


    }

    return new ResponseEntity<Object>("LA VENTA INGRESADA NO FUE ENCONTRADA", HttpStatus.NOT_FOUND);

  }

  @Transactional
  @PostMapping("/vendedores/{compraID}/rechazar")
  public @ResponseBody ResponseEntity<Object> rechazarCompra(@PathVariable("compraID") Integer compraID) {

    //BUSCAMOS LA COMPRA
    Optional<Compra> compra = repoCompra.findById(compraID);

    if (compra.isPresent()) {

      //CAMBIAMOS EL ESTADO DE LA COMPRA
      LocalDateTime fechaHora = LocalDateTime.now();

      RegistroEstadoCompra nuevoRegistro = new RegistroEstadoCompra(fechaHora, EstadoCompra.RECHAZADA, compra.get());
      repoRegistroCompra.save(nuevoRegistro);

      compra.get().getRegistroEstadosCompra().add(nuevoRegistro);
      repoCompra.save(compra.get());

      return new ResponseEntity<Object>("La venta fue rechazada", HttpStatus.ACCEPTED);
    }

    return new ResponseEntity<Object>("LA VENTA INGRESADA NO FUE ENCONTRADA", HttpStatus.NOT_FOUND);

  }
}




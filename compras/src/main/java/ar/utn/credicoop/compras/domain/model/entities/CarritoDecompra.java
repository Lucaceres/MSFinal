package ar.utn.credicoop.compras.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "CarritoDeCompra")
@Getter@Setter
public class CarritoDecompra extends Persistente{

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private List<Item> itemsAComprar;

    public CarritoDecompra() {
        this.itemsAComprar = new ArrayList<>();
    }

    public void agregarProducto(Item item){
        this.itemsAComprar.add(item);
    }


    public void eliminarProducto(Integer itemID) throws Exception {
        //BUSCAMOS EL ITEM POR ID
        Optional<Item> itemABorrar = this.itemsAComprar.stream().filter(item -> item.getId().equals(itemID)).findFirst();

        //SI LO ENCONTRAMOS LO BORRAMOS Y SINO TIRAMOS EXCEPCION
        if (itemABorrar.isPresent()) {
            this.itemsAComprar.remove(itemABorrar.get());
        }
        else {
            throw new Exception("El item no está en el carrito");
        }
    }

    public Double calcularTotal(){

        return this.itemsAComprar.stream().mapToDouble(Item::getPrecioItem).sum();
    }

    public Item ultimoItem() throws Exception {
        if(this.itemsAComprar.size() <=0){
            throw new Exception("El carrito está vacio!");
        }
        return this.itemsAComprar.get(itemsAComprar.size()-1);

    }

    public Integer vendedorElegidoId() throws Exception {
        return this.ultimoItem().getProductoPersonalizado();
    }


}

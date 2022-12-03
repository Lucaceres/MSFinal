package ar.utn.credicoop.compras.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Item")
@Getter
@Setter
public class Item extends Persistente{
    @Column(name = "cantidad")
    private int cantidad;

   // @ManyToOne(cascade = CascadeType.PERSIST)
    //@JoinColumn(name = "producto_personalizado_id", referencedColumnName = "id")
    @Column(name = "ProductoPersonalizadoId")
    private Integer productoPersonalizado;

    public Item() {}

    public Item(int cantidad, Integer productoPersonalizado) {
        this.cantidad = cantidad;
        this.productoPersonalizado = productoPersonalizado;
    }
//TODO RESOLVER ESTO(CALCULAR PRECIO CON PROXY)
    /*
    public Double calcularPrecio(){
        return this.productoPersonalizado.calcularPrecioFinal() * this.cantidad;
    }

     */
}

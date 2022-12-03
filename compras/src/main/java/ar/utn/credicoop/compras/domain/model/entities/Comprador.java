package ar.utn.credicoop.compras.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Comprador")
@Getter
@Setter
public class Comprador extends Persistente{
    @Column(name = "nombre")
    private String nombre;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Comprador_id", referencedColumnName = "id")
    private List<CarritoDecompra> carritosDeCompra;


    public Comprador() {
        this.carritosDeCompra = new ArrayList<>();
        this.carritosDeCompra.add(new CarritoDecompra());
    }

    public Comprador(String nombre)
    {
        this.nombre = nombre;
        this.carritosDeCompra = new ArrayList<>();
        this.carritosDeCompra.add(new CarritoDecompra());
    }


    public CarritoDecompra getCarritoActual() {
        int tamanio = this.carritosDeCompra.size();
        return this.carritosDeCompra.get(tamanio-1);
    }
}

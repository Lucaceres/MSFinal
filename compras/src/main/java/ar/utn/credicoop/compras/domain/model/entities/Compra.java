package ar.utn.credicoop.compras.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Compras")
@Getter
@Setter
public class Compra extends Persistente {

    //@ManyToOne
    //Esta en otro ms
    @Column(name = "VendedorId")
    private Integer vendedor;

    //Esta en otro ms
    @Column(name = "MetodoDePagoId")
    private Integer metodoDePago;

    @OneToOne
    private CarritoDecompra carritoDeCompra;

    @OneToMany(mappedBy = "compra",cascade = CascadeType.PERSIST)
    private List<RegistroEstadoCompra> registroEstadosCompra;

    @ManyToOne
    private Comprador comprador;


    public Compra() {
        this.registroEstadosCompra = new ArrayList<>();
    }

    public Compra(Integer metodoDePago, CarritoDecompra carritoDeCompra, Comprador comprador, Integer vendedor) {
        this.metodoDePago = metodoDePago;
        this.carritoDeCompra = carritoDeCompra;
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.registroEstadosCompra = new ArrayList<>();
    }

    public RegistroEstadoCompra getEstadoActual(){
        int tamanio = this.registroEstadosCompra.size();
        return this.registroEstadosCompra.get(tamanio-1);
    }

    public Double precioCompra(){
        return this.carritoDeCompra.calcularTotal();
    }
}

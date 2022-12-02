package ar.utn.credicoop.productopersonalizado.domain.model.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Producto_Personalizado")
@Setter
@Getter
public class ProductoPersonalizado extends Persistente{
    @Column(name = "Publicado")
    private Boolean estaPublicado;

    //@NotNull
    //@ManyToOne
    @Column(name = "ProductoBase_id")
    private Integer productoBase;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Producto_Personalizado_id", referencedColumnName = "id")
    private List<Personalizacion> personalizaciones;

    //@ManyToOne
    //@JoinColumn(name = "vendedor_id")
    @Column(name = "Vendedor_id")
    private Integer vendedor;

    public ProductoPersonalizado() {
        this.personalizaciones = new ArrayList<>();
    }

    public ProductoPersonalizado(Integer productoBase, Integer vendedor,List<Personalizacion>personalizaciones) {
        this.productoBase = productoBase;
        this.vendedor = vendedor;
        this.personalizaciones = personalizaciones;
        this.estaPublicado = false;
    }
//TODO ARREGLAR ENCAPSULAMIENTO

    public Double calcularPrecioFinal(double precioBase){
        return precioBase + this.precioDeLasPersonalizaciones();
    }



    public Double precioDeLasPersonalizaciones() {
        return this.personalizaciones.stream().mapToDouble(p -> p.getPrecio()).sum();
    }

    public void agregarPersonalizacion(Personalizacion personalizacion) {
        this.personalizaciones.add(personalizacion);
    }
}

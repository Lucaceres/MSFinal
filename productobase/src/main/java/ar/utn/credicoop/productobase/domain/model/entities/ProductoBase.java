package ar.utn.credicoop.productobase.domain.model.entities;

import ar.utn.credicoop.productobase.domain.DTOs.Personalizacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "Producto_Base")
@Setter
@Getter
public class ProductoBase extends Persistente{
    @Column(name = "nombre")
    private String nombre;

    @Column(name = "Precio_Base")
    private Double precioBase;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "Tiempo_Fabricacion")
    private String tiempoFabricacion;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ProductoBase_id", referencedColumnName = "id")
    private List<AreaPorProductoBase> areas;

    public ProductoBase() {}

    public ProductoBase(String nombre, Double precioBase, String descripcion, String tiempoFabricacion, List<AreaPorProductoBase> areas) {
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.descripcion = descripcion;
        this.tiempoFabricacion = tiempoFabricacion;
        this.areas = areas;
    }
    public void agregarAreaPorProductoBase(AreaPorProductoBase area){
        this.areas.add(area);
    }

//TODO ARREGLAR EL ENCAPSULAMIENTO
    //ARREGLADO

    public Boolean validarPersos(List<Personalizacion> personalizaciones)
    {
        //return this.areas.stream().anyMatch(areaPorProductoBase -> areaPorProductoBase.validarPersonalizacion(personalizacion));
        for(Personalizacion personalizacion:personalizaciones)
        {
            this.areas.stream().anyMatch(areaPorProductoBase -> areaPorProductoBase.validarPersonalizacion(personalizacion));
        }
        return personalizaciones.stream().allMatch(personalizacion -> this.areas.stream().anyMatch(areaPorProductoBase -> areaPorProductoBase.validarPersonalizacion(personalizacion)));
    }
}

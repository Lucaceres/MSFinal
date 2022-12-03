package ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion;

import ar.utn.credicoop.productopersonalizado.domain.model.entities.Persistente;
import ar.utn.credicoop.productopersonalizado.domain.model.entities.ProductoPersonalizado;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Publicacion")
@Setter
@Getter
public class Publicacion extends Persistente {

    @Column(name = "nombre")
    private String nombre;

    @OneToOne
    private ProductoPersonalizado productoPublicado;

    //Bidireccional
    @OneToMany(mappedBy = "publicacion",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<RegistroEstadoPublicacion> registrosEstados;

    public Publicacion() {
        this.registrosEstados = new ArrayList<>();
    }

    public Publicacion(String nombre, ProductoPersonalizado productoPublicado) {
        this.nombre = nombre;
        this.productoPublicado = productoPublicado;
        this.registrosEstados = new ArrayList<>();
    }

    public RegistroEstadoPublicacion getEstadoActual(){

        int tamanio = this.registrosEstados.size();
        return this.registrosEstados.get(tamanio-1);
    }

    public void agregarRegistro(RegistroEstadoPublicacion registroEstadoPublicacion){
        this.registrosEstados.add(registroEstadoPublicacion);
    }
}

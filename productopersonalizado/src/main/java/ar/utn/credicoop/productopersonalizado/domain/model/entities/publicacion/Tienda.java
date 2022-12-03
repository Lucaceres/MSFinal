package ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion;

import ar.utn.credicoop.productopersonalizado.domain.model.entities.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tienda")
@Getter
@Setter
public class Tienda extends Persistente {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "Tienda_id", referencedColumnName = "id")
    private List<Publicacion> publicaciones;

    public Tienda() {
        this.publicaciones = new ArrayList<>();
    }

    public void agregarPublicacion(Publicacion publicacion){
        this.publicaciones.add(publicacion);
    }

}

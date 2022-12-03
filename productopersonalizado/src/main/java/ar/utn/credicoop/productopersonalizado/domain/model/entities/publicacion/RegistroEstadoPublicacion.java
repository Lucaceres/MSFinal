package ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Resgistro_estado_publicacion")
@Getter
@Setter
public class RegistroEstadoPublicacion extends Persistence{
    @Column(name = "fecha")
    private LocalDateTime fechaHoraPublicacion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoPublicacion;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;


    public RegistroEstadoPublicacion() {}

    public RegistroEstadoPublicacion(LocalDateTime fechaHoraPublicacion, EstadoPublicacion estadoPublicacion, Publicacion publicacion) {
        this.fechaHoraPublicacion = fechaHoraPublicacion;
        this.estadoPublicacion = estadoPublicacion;
        this.publicacion = publicacion;
    }
}

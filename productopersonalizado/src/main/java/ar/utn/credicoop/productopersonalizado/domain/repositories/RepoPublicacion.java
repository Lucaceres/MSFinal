package ar.utn.credicoop.productopersonalizado.domain.repositories;

import ar.utn.credicoop.productopersonalizado.domain.model.entities.publicacion.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "publicaciones")
public interface RepoPublicacion extends JpaRepository<Publicacion, Integer> {
    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(Publicacion publicacion);
}

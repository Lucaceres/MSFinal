package ar.utn.credicoop.productobase.domain.repositories;

import ar.utn.credicoop.productobase.domain.model.entities.TipoPersonalizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "tipopersonalizacion")
public interface RepoTIpoPersonalizacion extends JpaRepository<TipoPersonalizacion,Integer> {
    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(TipoPersonalizacion tipoPersonalizacion);
}

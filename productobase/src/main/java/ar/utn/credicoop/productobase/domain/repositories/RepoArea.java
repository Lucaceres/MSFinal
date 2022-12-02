package ar.utn.credicoop.productobase.domain.repositories;

import ar.utn.credicoop.productobase.domain.model.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "areas")
public interface RepoArea extends JpaRepository<Area,Integer> {
    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(Area area);
}

package ar.utn.credicoop.compras.domain.repositories;

import ar.utn.credicoop.compras.domain.model.entities.Comprador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
@RepositoryRestResource(path = "compradores")
public interface RepoComprador extends JpaRepository<Comprador,Integer> {
    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(Comprador comprador);
}

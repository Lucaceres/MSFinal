package ar.utn.credicoop.compras.domain.repositories;

import ar.utn.credicoop.compras.domain.model.entities.RegistroEstadoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
@RepositoryRestResource(path = "registrocompras")
public interface RepoRegistroCompra extends JpaRepository<RegistroEstadoCompra, Integer> {

    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(RegistroEstadoCompra registroEstadoCompra);
}

package ar.utn.credicoop.productopersonalizado.domain.repositories;

import ar.utn.credicoop.productopersonalizado.domain.model.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "vendedores")
public interface RepoVendedor extends JpaRepository<Vendedor, Integer> {
    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(Vendedor vendedor);


}

package ar.utn.credicoop.productopersonalizado.domain.repositories;

import ar.utn.credicoop.productopersonalizado.domain.model.entities.ProductoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "productopersonalizado")
public interface RepoProductoPersonalizado extends JpaRepository<ProductoPersonalizado,Integer> {
    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(ProductoPersonalizado productoPersonalizado);
}

package ar.utn.credicoop.productobase.domain.repositories;

import ar.utn.credicoop.productobase.domain.model.entities.ProductoBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "productobase")
public interface RepoProductoBase extends JpaRepository<ProductoBase, Integer> {

    //public ProductoBase findProductoBaseByNombre(String nombre);

    @RestResource(exported = false)
    void deleteById(Integer id);

    @RestResource(exported = false)
    void delete(ProductoBase productoBase);
}

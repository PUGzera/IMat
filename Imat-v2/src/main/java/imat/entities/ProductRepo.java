package imat.entities;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//Use methods from crudrepo to get products from database
public interface ProductRepo extends CrudRepository<Product, Long> {

    Iterable<Product> findAllByCategoryIgnoreCase(String category);

    Iterable<Product> findByIsEcological(Boolean ecological);

    Product findByName(String name);

    //@Query("select p from Product p where p.name like :{?1}")
    Iterable<Product> findByNameContainingIgnoreCase(String search);
}

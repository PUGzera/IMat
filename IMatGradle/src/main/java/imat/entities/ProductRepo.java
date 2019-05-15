package imat.entities;

import org.springframework.data.repository.CrudRepository;

//Use methods from crudrepo to get products from database
public interface ProductRepo extends CrudRepository<Product, Long> {

    Iterable<Product> findAllByCategory(String category);

    Iterable<Product> findAllByIsEcological(Boolean ecological);

    Product findByName(String name);

}

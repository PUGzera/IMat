package imat.entities;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepo extends CrudRepository<Order, Long> {

    Iterable<Order> findAllByOrderByLocalDate();

}

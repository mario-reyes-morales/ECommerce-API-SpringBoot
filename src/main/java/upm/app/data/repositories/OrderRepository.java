package upm.app.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import upm.app.data.models.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByOwnerId(Integer ownerId);

    List<Order> findDistinctByLinesProductId(Integer articleId);
}

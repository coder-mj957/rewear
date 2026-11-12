package Rewear;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get only this customer's orders.
    List<Order> findByUser_EmailOrderByOrderDateDesc(String email);

    // Find an order only if it belongs to this customer.
    Optional<Order> findByIdAndUser_Email(Long id, String email);
}
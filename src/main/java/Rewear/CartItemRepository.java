package Rewear;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Get only the logged-in customer's cart.
    List<CartItem> findByUser_Email(String email);

    // Find an item only if it belongs to the logged-in customer.
    Optional<CartItem> findByIdAndUser_Email(Long id, String email);

    // Check whether the product is already in this customer's cart.
    Optional<CartItem> findByUser_EmailAndProduct_Id(
            String email,
            Long productId
    );
}
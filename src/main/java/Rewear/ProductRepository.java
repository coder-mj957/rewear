
package Rewear;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    Optional<Product> findByIdAndActiveTrue(Long id);

    @Query("SELECT p FROM Product p WHERE " +
            "p.active = true AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:category IS NULL OR :category = '' OR p.category = :category) " +
            "AND (:size IS NULL OR :size = '' OR p.size = :size)")
    List<Product> searchProducts(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("size") String size,
            Sort sort
    );
}


package Rewear;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;

    public ProductService(
            ProductRepository productRepository,
            AppUserRepository appUserRepository) {

        this.productRepository = productRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdAndActiveTrue(id);
    }

    // Add a product and automatically assign its seller.
    @Transactional
    public Product addProduct(Product product, String email) {

        AppUser seller = appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        // Prevent a submitted form from assigning another product ID.
        product.setId(null);
        product.setSeller(seller);
        product.setActive(true);

        return productRepository.save(product);
    }

    // Update only if the logged-in user owns the product or is admin.
    @Transactional
    public Product updateProduct(Product submittedProduct, String email) {

        if (submittedProduct.getId() == null) {
            throw new IllegalArgumentException("Product ID is missing");
        }

        Product existingProduct = productRepository
                .findByIdAndActiveTrue(submittedProduct.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        checkOwnership(existingProduct, email);

        // Update editable fields only.
        // Keep the original seller, ID and active status.
        existingProduct.setName(submittedProduct.getName());
        existingProduct.setDescription(submittedProduct.getDescription());
        existingProduct.setPrice(submittedProduct.getPrice());
        existingProduct.setSize(submittedProduct.getSize());
        existingProduct.setCategory(submittedProduct.getCategory());
        existingProduct.setCondition(submittedProduct.getCondition());
        existingProduct.setImageUrl(submittedProduct.getImageUrl());

        return productRepository.save(existingProduct);
    }

    // Soft delete only if the logged-in user owns the product or is admin.
    @Transactional
    public void deleteProduct(Long id, String email) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        checkOwnership(product, email);

        product.setActive(false);
        productRepository.save(product);
    }

    // Check permission for a single product.
    @Transactional(readOnly = true)
    public boolean canManageProduct(Long id, String email) {

        if (email == null) {
            return false;
        }

        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElse(null);

        if (product == null) {
            return false;
        }

        AppUser user = appUserRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        return isAdmin(user) ||
                (product.getSeller() != null &&
                        email.equalsIgnoreCase(product.getSeller().getEmail()));
    }

    // IDs of products that the current user is allowed to manage.
    @Transactional(readOnly = true)
    public Set<Long> getManageableProductIds(String email) {

        AppUser user = appUserRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return Set.of();
        }

        List<Product> products = productRepository.findByActiveTrue();

        if (isAdmin(user)) {
            return products.stream()
                    .map(Product::getId)
                    .collect(Collectors.toSet());
        }

        return products.stream()
                .filter(product ->
                        product.getSeller() != null &&
                                email.equalsIgnoreCase(
                                        product.getSeller().getEmail()
                                )
                )
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    private void checkOwnership(Product product, String email) {

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        if (isAdmin(user)) {
            return;
        }

        if (product.getSeller() == null ||
                !email.equalsIgnoreCase(product.getSeller().getEmail())) {

            throw new AccessDeniedException(
                    "You are not allowed to modify this product"
            );
        }
    }

    private boolean isAdmin(AppUser user) {
        String role = user.getRole();

        return "ADMIN".equalsIgnoreCase(role) ||
                "ROLE_ADMIN".equalsIgnoreCase(role);
    }

    // Search and sort active products.
    public List<Product> searchProducts(
            String keyword,
            String category,
            String size,
            String sortBy) {

        Sort sort = Sort.unsorted();

        if ("priceLow".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.ASC, "price");
        } else if ("priceHigh".equals(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "price");
        }

        return productRepository.searchProducts(
                keyword, category, size, sort
        );
    }
}

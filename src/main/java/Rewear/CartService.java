package Rewear;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;

    public CartService(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            AppUserRepository appUserRepository) {

        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.appUserRepository = appUserRepository;
    }

    // Add product to the logged-in customer's cart.
    @Transactional
    public void addToCart(Long productId, String email) {

        Product product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product is unavailable"));

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        // If the product is already in the cart, increase its quantity.
        CartItem existingItem = cartItemRepository
                .findByUser_EmailAndProduct_Id(email, productId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = new CartItem(product, 1, user);
            cartItemRepository.save(cartItem);
        }
    }

    // Get only this customer's cart items.
    public List<CartItem> getCartItems(String email) {
        return cartItemRepository.findByUser_Email(email);
    }

    // Remove an item only if it belongs to this customer.
    @Transactional
    public void removeFromCart(Long id, String email) {

        cartItemRepository.findByIdAndUser_Email(id, email)
                .ifPresent(cartItemRepository::delete);
    }

    // Increase quantity only for this customer's item.
    @Transactional
    public void increaseQuantity(Long id, String email) {

        cartItemRepository.findByIdAndUser_Email(id, email)
                .ifPresent(item -> {
                    item.setQuantity(item.getQuantity() + 1);
                    cartItemRepository.save(item);
                });
    }

    // Decrease quantity only for this customer's item.
    @Transactional
    public void decreaseQuantity(Long id, String email) {

        cartItemRepository.findByIdAndUser_Email(id, email)
                .ifPresent(item -> {

                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                        cartItemRepository.save(item);
                    } else {
                        cartItemRepository.delete(item);
                    }
                });
    }

    // Calculate only this customer's cart total.
    public BigDecimal getCartTotal(String email) {

        List<CartItem> cartItems = getCartItems(email);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);
        }

        return total;
    }
}
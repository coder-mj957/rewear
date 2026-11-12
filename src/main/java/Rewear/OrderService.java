
package Rewear;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartItemRepository cartItemRepository,
            AppUserRepository appUserRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.appUserRepository = appUserRepository;
        this.productRepository = productRepository;
    }

    // Existing cart checkout. Keep this functionality.
    @Transactional
    public Order placeOrder(
            String name,
            String email,
            String phone,
            String address,
            String userEmail) {

        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByUser_Email(userEmail);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);
        }

        Order order = new Order(
                name, email, phone, address, total, user
        );

        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem(
                    order,
                    item.getProduct(),
                    item.getQuantity(),
                    item.getProduct().getPrice()
            );

            orderItemRepository.save(orderItem);
        }

        // Empty only this customer's cart.
        cartItemRepository.deleteAll(cartItems);

        return order;
    }

    // NEW: Buy one product directly without using the cart.
    @Transactional
    public Order placeOrderForProduct(
            Long productId,
            String name,
            String email,
            String phone,
            String address,
            String userEmail) {

        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalStateException("User not found"));

        Product product = productRepository
                .findByIdAndActiveTrue(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product is unavailable"
                        ));

        BigDecimal total = product.getPrice();

        Order order = new Order(
                name,
                email,
                phone,
                address,
                total,
                user
        );

        order = orderRepository.save(order);

        // Direct order contains one product with quantity 1.
        OrderItem orderItem = new OrderItem(
                order,
                product,
                1,
                product.getPrice()
        );

        orderItemRepository.save(orderItem);

        // The customer's existing cart is not changed.
        return order;
    }

    // A customer can access only their own order confirmation.
    public Order getOrderById(Long id, String userEmail) {

        return orderRepository.findByIdAndUser_Email(id, userEmail)
                .orElse(null);
    }
}

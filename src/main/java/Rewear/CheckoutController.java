
package Rewear;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CheckoutController {

    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;

    public CheckoutController(
            OrderService orderService,
            CartService cartService,
            ProductService productService) {

        this.orderService = orderService;
        this.cartService = cartService;
        this.productService = productService;
    }

    // Existing cart checkout.
    @GetMapping("/checkout")
    public String showCheckout(
            Model model,
            Principal principal) {

        String userEmail = principal.getName();

        if (cartService.getCartItems(userEmail).isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute(
                "cartTotal",
                cartService.getCartTotal(userEmail)
        );

        return "checkout";
    }

    // Existing cart order placement.
    @PostMapping("/checkout/place-order")
    public String placeOrder(
            @RequestParam String customerName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            Principal principal) {

        try {
            Order order = orderService.placeOrder(
                    customerName,
                    email,
                    phone,
                    address,
                    principal.getName()
            );

            return "redirect:/order-confirmation/" + order.getId();

        } catch (IllegalStateException e) {
            return "redirect:/cart";
        }
    }

    // NEW: Display checkout for one selected product.
    @GetMapping("/checkout/buy-now/{productId}")
    public String showBuyNowCheckout(
            @PathVariable Long productId,
            Model model) {

        Product product = productService.getProductById(productId)
                .orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        return "buy-now-checkout";
    }

    // NEW: Place an order directly for the selected product.
    @PostMapping("/checkout/buy-now/{productId}")
    public String buyNow(
            @PathVariable Long productId,
            @RequestParam String customerName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            Principal principal) {

        try {
            Order order = orderService.placeOrderForProduct(
                    productId,
                    customerName,
                    email,
                    phone,
                    address,
                    principal.getName()
            );

            return "redirect:/order-confirmation/" + order.getId();

        } catch (IllegalArgumentException | IllegalStateException e) {
            return "redirect:/products/" + productId;
        }
    }

    // Order confirmation.
    @GetMapping("/order-confirmation/{id}")
    public String orderConfirmation(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        Order order = orderService.getOrderById(
                id,
                principal.getName()
        );

        if (order == null) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);

        return "order-confirmation";
    }
}

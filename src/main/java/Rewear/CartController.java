package Rewear;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Display the logged-in customer's shopping cart.
    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {

        String email = principal.getName();

        model.addAttribute(
                "cartItems",
                cartService.getCartItems(email)
        );

        model.addAttribute(
                "cartTotal",
                cartService.getCartTotal(email)
        );

        return "cart";
    }

    // Add product to cart.
    @PostMapping("/cart/add/{productId}")
    public String addToCart(
            @PathVariable Long productId,
            Principal principal) {

        cartService.addToCart(productId, principal.getName());

        return "redirect:/cart";
    }

    // Remove product from cart.
    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(
            @PathVariable Long id,
            Principal principal) {

        cartService.removeFromCart(id, principal.getName());

        return "redirect:/cart";
    }

    // Increase quantity.
    @PostMapping("/cart/increase/{id}")
    public String increaseQuantity(
            @PathVariable Long id,
            Principal principal) {

        cartService.increaseQuantity(id, principal.getName());

        return "redirect:/cart";
    }

    // Decrease quantity.
    @PostMapping("/cart/decrease/{id}")
    public String decreaseQuantity(
            @PathVariable Long id,
            Principal principal) {

        cartService.decreaseQuantity(id, principal.getName());

        return "redirect:/cart";
    }
}
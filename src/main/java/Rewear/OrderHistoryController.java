package Rewear;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderHistoryController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderHistoryController(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // Display only the logged-in customer's previous orders.
    @GetMapping("/orders")
    public String orderHistory(
            Model model,
            Principal principal) {

        model.addAttribute(
                "orders",
                orderRepository.findByUser_EmailOrderByOrderDateDesc(
                        principal.getName()
                )
        );

        return "order-history";
    }

    // Display details only for an order owned by this customer.
    @GetMapping("/orders/{id}")
    public String orderDetails(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        Order order = orderRepository
                .findByIdAndUser_Email(id, principal.getName())
                .orElse(null);

        if (order == null) {
            return "redirect:/orders";
        }

        List<OrderItem> items =
                orderItemRepository.findByOrder_Id(id);

        model.addAttribute("order", order);
        model.addAttribute("items", items);

        return "order-details";
    }
}
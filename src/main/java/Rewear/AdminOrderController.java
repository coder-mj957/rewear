package Rewear;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@Controller
public class AdminOrderController {

    private final OrderRepository orderRepository;

    public AdminOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Display all customer orders
    @GetMapping("/admin/orders")
    public String viewAllOrders(Model model) {

        model.addAttribute(
                "orders",
                orderRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "orderDate")
                )
        );

        model.addAttribute(
                "statuses",
                List.of("PLACED", "CONFIRMED", "SHIPPED",
                        "DELIVERED", "CANCELLED")
        );

        return "admin-orders";
    }

    // Update order status
    @PostMapping("/admin/orders/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        List<String> allowedStatuses = List.of(
                "PLACED", "CONFIRMED", "SHIPPED",
                "DELIVERED", "CANCELLED"
        );

        Order order = orderRepository.findById(id).orElse(null);

        if (order != null && allowedStatuses.contains(status)) {
            order.setStatus(status);
            orderRepository.save(order);
        }

        return "redirect:/admin/orders";
    }
}
package Rewear;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private String email;

    private String phone;

    private String address;

    private BigDecimal totalAmount;

    private String status;

    private LocalDateTime orderDate;

    // Customer who placed the order.
    // Nullable to preserve existing orders.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private AppUser user;

    public Order() {}

    public Order(
            String customerName,
            String email,
            String phone,
            String address,
            BigDecimal totalAmount) {

        this.customerName = customerName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.totalAmount = totalAmount;
        this.status = "PLACED";
        this.orderDate = LocalDateTime.now();
    }

    public Order(
            String customerName,
            String email,
            String phone,
            String address,
            BigDecimal totalAmount,
            AppUser user) {

        this(customerName, email, phone, address, totalAmount);
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
}
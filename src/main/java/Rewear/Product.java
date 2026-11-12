
package Rewear;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private String size;
    private String category;

    @Column(name = "product_condition")
    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Column(name = "image_url", length = 2000)
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    // The user who listed this product.
    // Nullable to preserve products already in the database.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = true)
    private AppUser seller;

    public enum Condition {
        NEW_WITH_TAGS,
        LIKE_NEW,
        GOOD,
        FAIR
    }

    public Product() {
    }

    public Product(Long id, String name,
                   String description, BigDecimal price,
                   String size, String category,
                   Condition condition, String imageUrl) {

        this.id = id;
        this.name = name;
        this.description = description;
        setPrice(price);
        this.size = size;
        this.category = category;
        this.condition = condition;
        this.imageUrl = imageUrl;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be null or negative"
            );
        }
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AppUser getSeller() {
        return seller;
    }

    public void setSeller(AppUser seller) {
        this.seller = seller;
    }
}

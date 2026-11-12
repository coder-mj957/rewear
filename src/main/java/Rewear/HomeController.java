
package Rewear;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Display products and identify which ones this user can manage.
    @GetMapping("/products")
    public String showProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            Model model,
            Principal principal) {

        model.addAttribute(
                "products",
                productService.searchProducts(
                        keyword, category, size, sortBy
                )
        );

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedSize", size);
        model.addAttribute("selectedSort", sortBy);

        Set<Long> manageableIds = principal == null
                ? Set.of()
                : productService.getManageableProductIds(
                principal.getName()
        );

        model.addAttribute("manageableProductIds", manageableIds);

        return "products";
    }

    // Product details.
    @GetMapping("/products/{id}")
    public String productDetails(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        Product selectedProduct =
                productService.getProductById(id).orElse(null);

        if (selectedProduct == null) {
            return "redirect:/products";
        }

        boolean canManage = principal != null &&
                productService.canManageProduct(
                        id, principal.getName()
                );

        model.addAttribute("product", selectedProduct);
        model.addAttribute("canManageProduct", canManage);

        return "product-details";
    }

    // Add product form.
    @GetMapping("/products/new")
    public String showAddProductForm(Model model) {

        model.addAttribute("product", new Product());

        return "add-product";
    }

    // Edit form: check ownership before displaying it.
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        Product product = productService.getProductById(id)
                .orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        if (!productService.canManageProduct(
                id, principal.getName())) {
            return "redirect:/products?error=not-owner";
        }

        model.addAttribute("product", product);

        return "edit-product";
    }

    // Add product and assign the logged-in user as seller.
    @PostMapping("/products")
    public String addProduct(
            @ModelAttribute Product product,
            Principal principal) {

        productService.addProduct(product, principal.getName());

        return "redirect:/products";
    }

    // Update product after checking ownership.
    @PostMapping("/products/update")
    public String updateProduct(
            @ModelAttribute Product product,
            Principal principal) {

        productService.updateProduct(product, principal.getName());

        return "redirect:/products";
    }

    // Delete product after checking ownership.
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            Principal principal) {

        productService.deleteProduct(id, principal.getName());

        return "redirect:/products";
    }
}

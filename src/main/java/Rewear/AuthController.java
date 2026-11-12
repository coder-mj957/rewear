package Rewear;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        if (name.isBlank() || email.isBlank()
                || password.length() < 8) {
            model.addAttribute(
                    "error",
                    "Enter valid details. Password must have at least 8 characters."
            );
            return "register";
        }

        try {
            userService.registerUser(
                    name.trim(),
                    email.trim().toLowerCase(),
                    password
            );

            return "redirect:/login?registered";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
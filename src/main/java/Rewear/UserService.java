package Rewear;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String name, String email,
                             String password) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered!"
            );
        }

        AppUser user = new AppUser(
                name,
                email,
                passwordEncoder.encode(password),
                "CUSTOMER"
        );

        userRepository.save(user);
    }
}
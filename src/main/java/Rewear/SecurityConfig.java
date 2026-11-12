
package Rewear;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            AppUserRepository userRepository) {

        return email -> userRepository.findByEmail(email)
                .map(user -> {

                    String role = user.getRole();

                    // Avoid ROLE_ROLE_ADMIN if the database
                    // already contains the ROLE_ prefix.
                    if (role.startsWith("ROLE_")) {
                        role = role.substring(5);
                    }

                    return User.withUsername(user.getEmail())
                            .password(user.getPassword())
                            .roles(role)
                            .build();
                })
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/register",
                                "/login",
                                "/css/**",
                                "/images/**"
                        ).permitAll()

                        // Product creation and editing require login.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/products/new",
                                "/products/edit/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/products",
                                "/products/update",
                                "/products/delete/**"
                        ).authenticated()

                        // Public product browsing.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/products",
                                "/products/**"
                        ).permitAll()

                        // Admin-only pages.
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}

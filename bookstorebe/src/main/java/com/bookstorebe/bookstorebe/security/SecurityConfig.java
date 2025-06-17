package com.bookstorebe.bookstorebe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF as you had it
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/req/signup",
                    "/js/**",
                    "/css/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll() // These paths are permitted without authentication
                .anyRequest().authenticated() // All other requests require authentication
            )
            .formLogin(httpForm -> {
                httpForm.loginPage("/req/login").permitAll(); // The login page itself is permitted
                httpForm.defaultSuccessUrl("/index"); // Redirect after successful login
                // ADD THIS LINE: Explicitly set the URL where the login form will be POSTed
                httpForm.loginProcessingUrl("/login");
            })
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL to trigger logout
                .logoutSuccessUrl("/req/login?logout") // Redirect after successful logout
                .permitAll() // Logout is permitted for all
            );

        return http.build();
    }
}

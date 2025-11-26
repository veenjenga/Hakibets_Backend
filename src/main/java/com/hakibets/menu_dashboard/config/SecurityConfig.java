package com.hakibets.menu_dashboard.config;

import com.hakibets.menu_dashboard.security.JwtAuthenticationFilter;
import com.hakibets.menu_dashboard.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Date;

@Configuration //Tells spring that the class is a special instruction manual for setting up the app
@EnableWebSecurity //Turns on spring security
@Profile("custom-security") //security rules only apply if the app runs with the "custom-security" profile (set in application.properties with spring.profiles.active=custom-security).
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomAuthenticationProvider customAuthenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
        System.out.println("SecurityConfig initialized at " + new Date());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //sets up the security chain using an HttpSecurity object (a tool from Spring to define rules).
        System.out.println("Configuring SecurityFilterChain at " + new Date());
        http
                .csrf(csrf -> { //Cross-Site Request Forgery protection
                    System.out.println("Disabling CSRF at " + new Date());
                    csrf.disable();
                })
                /*.sessionManagement(session -> ...) controls how the app handles user sessions (like keeping track of who’s logged in).
                sessionCreationPolicy(SessionCreationPolicy.STATELESS) sets it to “stateless,” meaning the app won’t remember users between requests.
                Instead, each request must include a token, which is better for APIs.
                 */
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    System.out.println("Permitting /api/auth/** and securing others at " + new Date());
                    auth
                            .requestMatchers("/api/auth/**").permitAll()        // Public endpoints: register & login
                            .requestMatchers("/h2-console/**").permitAll()      // Allow H2 console in browser
                            .anyRequest().authenticated();
                })
                //This lets the filter check the JWT token first, ensuring only valid token holders get through to protected areas.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // THIS LINE IS THE ONLY FIX NEEDED FOR 403 on POST requests (register/login)
                // Spring Boot 3.5 + Spring Security 6 blocks X-Frame-Options by default → 403 on all POSTs even with permitAll()
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build(); // finalizes the security chain and returns it to Spring to use. It’s like locking in all the rules.
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider);
    }
}
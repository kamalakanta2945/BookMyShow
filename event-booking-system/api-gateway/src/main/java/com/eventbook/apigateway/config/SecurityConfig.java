package com.eventbook.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // PUBLIC Endpoints
                        .pathMatchers(
                                "/api/v1/auth/**",
                                "/eureka/**",
                                "/api/v1/movies", // Allow browsing movies
                                "/api/v1/events"  // Allow browsing events
                        ).permitAll()

                        // ADMIN Endpoints
                        .pathMatchers(
                                "/api/v1/users/admin/**",
                                "/api/v1/theatres/**",
                                "/api/v1/offers/admin/**",
                                "/api/v1/movies/**", // Admin can manage movies
                                "/api/v1/events/admin/**" // Admin can manage events
                        ).hasRole("ADMIN")

                        // ORGANIZER Endpoints
                        .pathMatchers(
                                "/api/v1/events/organizer/**" // Organizer can manage their events
                        ).hasRole("ORGANIZER")

                        // CUSTOMER Endpoints
                        .pathMatchers(
                                "/api/v1/bookings/**",
                                "/api/v1/payments/**",
                                "/api/v1/reviews/**"
                        ).hasRole("CUSTOMER")

                        // Any other request must be authenticated
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtDecoder(reactiveJwtDecoder())));
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        byte[] secretKeyBytes = jwtSecret.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }
}
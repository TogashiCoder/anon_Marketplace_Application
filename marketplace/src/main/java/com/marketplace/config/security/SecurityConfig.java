package com.marketplace.config.security;

import com.marketplace.service.securityService.UserServiceImpl;
import com.marketplace.service.securityService.exception.CustomAccessDeniedHandler;
import com.marketplace.service.securityService.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserServiceImpl userServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/login",
                                                "/api/admins/register",
                                               "/api/buyers/register",
                                              "/api/sellers/register",
                                              "/api/sellers/by/**",
                                             "/api/ratings/**",
                                             "/send-email",
                                            "/send-html-email",
                                            "/api/Security/**"
                                        )
                                .permitAll()
                                .requestMatchers("/api/categories/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/categories/**").permitAll()
                                .requestMatchers("/api/products/create", "/api/products/edit/**").hasAuthority("SELLER")
                                .requestMatchers("/api/products/delete/**").hasAnyAuthority("ADMIN", "SELLER")
                                .requestMatchers("/api/products/**").permitAll()
                                .requestMatchers("/api/equipment/admin/**", "/api/faults/admin").hasAnyAuthority("BUYER", "ADMIN", "SELLER")
                                .requestMatchers("/api/favorites/**").hasAuthority("BUYER")
                                .requestMatchers("/api/productsV/**").permitAll()
                                .requestMatchers("/api/followers/follow","/api/followers/unfollow/**").hasAuthority("BUYER")
                                .requestMatchers("/api/followers").hasAuthority("SELLER")
                                .requestMatchers("/api/followers/seller/**",
                                                "/api/followers/buyer/**",
                                                "/api/followers/check/**").permitAll()

                                .requestMatchers("/api/cart/**").hasAuthority("BUYER")
                                .requestMatchers("/api/coupons/**").permitAll()
                                .requestMatchers("/api/orders/**").permitAll()
                                .requestMatchers("/api/ordersConfirmation/**").permitAll()
                                .requestMatchers("/api/product-stock/**").permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .userDetailsService(userServiceImpl)
                .exceptionHandling(e -> e
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        e -> e.accessDeniedHandler(
                                        (request, response, accessDeniedException) -> response.setStatus(403)
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://yourdomain.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
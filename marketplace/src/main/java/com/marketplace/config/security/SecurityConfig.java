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

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    private final UserServiceImpl userServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;



//        @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                //1
//                .csrf(AbstractHttpConfigurer::disable)
//                //2
//                .authorizeHttpRequests(
//                        requset->requset.anyRequest().permitAll()
//                )
//                .build();
//    }









    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                //1
                .csrf(AbstractHttpConfigurer::disable)
                //2
                .authorizeHttpRequests(
                        req->req.
                                 requestMatchers("/login","/api/admins/register","/api/buyers/register","/api/sellers/register")
                                .permitAll()
                                .requestMatchers("/api/categories/admin/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/categories/**").permitAll()
                                .requestMatchers("/api/products/create","/api/products/edit/**").hasAuthority("SELLER")
                                .requestMatchers("/api/products/delete/**").hasAnyAuthority("ADMIN","SELLER")
                                .requestMatchers("/api/products/**").permitAll()
                                .requestMatchers("/api/equipment/admin/**","/api/faults/admin").hasAnyAuthority("BUYER","ADMIN","SELLER")
                                .anyRequest()
                                .authenticated()
                )
                //3
                .userDetailsService(userServiceImpl)
                //4
                .exceptionHandling(e->e.accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                //5
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //6
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //7
                .exceptionHandling(
                        e->e.accessDeniedHandler(
                                        (request, response, accessDeniedException)->response.setStatus(403)
                                )
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                //8
//                .logout(l->l
//                        .logoutUrl("/logout")
//                        .addLogoutHandler(customLogoutHandler)
//                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
//                        ))
                .build();

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

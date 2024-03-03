package com.ecommerce.shopapp.config;

import com.ecommerce.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] LIST_URL = {
            "/api/categories/**",
            "/api/products/**",
            "/api/images/**",
            "/api/sizes/**",
            "/api/colors/**",
    };

    private final JwtFilter jwtFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(String.format("%s/auth/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,LIST_URL).permitAll()

                            .requestMatchers(POST,LIST_URL).hasAnyRole(Role.ADMIN)

                            .requestMatchers(PUT,LIST_URL).hasAnyRole(Role.ADMIN)

                            .requestMatchers(DELETE,LIST_URL).hasAnyRole(Role.ADMIN)

                            .requestMatchers(GET,
                                    String.format("%s/users/**", apiPrefix)).hasAnyRole(Role.USER)

                            .requestMatchers(PUT,
                                    String.format("%s/users/**", apiPrefix)).hasAnyRole(Role.USER)

                            .requestMatchers(POST,
                                    String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)

                            .requestMatchers(GET,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()

                            .requestMatchers(PUT,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(DELETE,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                            .anyRequest().authenticated();

                })
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
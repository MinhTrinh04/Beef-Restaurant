package com.eshop.UserService.Config;

import com.eshop.UserService.Exception.CustomAccessDeniedHandler;
import com.eshop.UserService.Exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/actuator/**", "/h2-console/**", "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll()
                                                // Public endpoints - không cần token
                                                .requestMatchers(HttpMethod.POST, "/api/v1/users/register",
                                                                "/api/v1/users/login")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/v1/users/refresh-token")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/v1/users/logout").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/users/check-email")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/v1/users/resend-verification")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/users/verification-status")
                                                .permitAll()
                                                // Admin auth endpoints - public (login, refresh, logout)
                                                .requestMatchers(HttpMethod.POST,
                                                                "/api/v1/admin/auth/login",
                                                                "/api/v1/admin/auth/refresh-token",
                                                                "/api/v1/admin/auth/logout")
                                                .permitAll()
                                                // Authenticated endpoints - cần token
                                                .requestMatchers(HttpMethod.GET, "/api/v1/users/me").authenticated()
                                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/me").authenticated()
                                                // Admin endpoints - cần ADMIN role
                                                .requestMatchers(HttpMethod.GET, "/api/v1/users/admin/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/admin/**")
                                                .hasRole("ADMIN")
                                                .anyRequest().authenticated())

                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))

                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                                .accessDeniedHandler(new CustomAccessDeniedHandler()));
                http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

                return http.build();
        }
}
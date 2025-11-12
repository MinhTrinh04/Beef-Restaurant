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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Bật @PreAuthorize...
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Tạo bộ chuyển đổi JWT và gán Role Converter
        // Điều này LẤY TỪ section_15
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        http
                // 1. Cấu hình Session là STATELESS (quan trọng cho API)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. Tắt CSRF (phù hợp với API stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Cấu hình CORS (dùng bean bên dưới)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 4. Cấu hình ủy quyền (Authorization)
                .authorizeHttpRequests(authorize -> authorize
                        // Cho phép CORS Pre-flight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Cho phép Health Checks (nếu bạn dùng Actuator)
                        .requestMatchers("/actuator/**").permitAll()
                        // Tất cả API user đều phải xác thực
                        .requestMatchers("/api/v1/users/**").authenticated()
                        // Bất kỳ request nào khác cũng yêu cầu xác thực
                        .anyRequest().authenticated()
                )

                // 5. Cấu hình là Resource Server, dùng JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)) // Áp dụng Role Converter
                )

                // 6. Cấu hình xử lý lỗi JSON 401 và 403 (LẤY TỪ section_15)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Cần thay đổi URL này cho môi trường PROD
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // Giả sử frontend của bạn ở port 3000
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        // Cần expose "Authorization" nếu bạn muốn frontend đọc được token
        config.setExposedHeaders(Collections.singletonList("Authorization"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
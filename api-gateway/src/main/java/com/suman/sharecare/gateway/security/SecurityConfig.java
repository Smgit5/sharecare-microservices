package com.suman.sharecare.gateway.security;

import com.suman.sharecare.gateway.enums.UserRole;
import com.suman.sharecare.gateway.exception.JwtAccessDeniedHandler;
import com.suman.sharecare.gateway.exception.JwtAuthenticationEntryPoint;
import com.suman.sharecare.gateway.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        ex -> ex
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(
                    auth -> auth
                            .requestMatchers(HttpMethod.GET, "/donations/my/campaign/**").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.GET, "/donations/my").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.POST, "/donations").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.GET, "/campaigns/my").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.POST, "/campaigns").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.PUT, "/campaigns/**").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.PATCH, "/campaigns/*/approve", "/campaigns/*/reject", "/campaigns/*/send-back").hasRole(UserRole.ADMIN.name())
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login", "/auth/refresh", "/auth/logout", "/auth/forgot-password").permitAll()
                            .requestMatchers(HttpMethod.GET, "/auth/verify-email/**").permitAll()
                            .requestMatchers("/auth/test/**").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/auth/reset-password").permitAll()
                            .requestMatchers(HttpMethod.GET, "/campaigns/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/campaigns/filter/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/donations/test").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/donations/payment-status/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/donations/razorpay/verify").permitAll()
                            .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

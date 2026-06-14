package com.suman.sharecare.gateway.security;

import com.suman.sharecare.gateway.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                    auth -> auth
                            .requestMatchers("/auth/register", "/auth/login").permitAll()
                            .requestMatchers(HttpMethod.GET, "/campaigns/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/campaigns").hasRole("NGO_REP")
                            .requestMatchers(HttpMethod.PUT, "/campaigns/**").hasRole("NGO_REP")
                            .requestMatchers(HttpMethod.DELETE, "/campaigns/**").hasRole("NGO_REP")
                            .requestMatchers(HttpMethod.PATCH, "/campaigns/*/approve", "/campaigns/*/reject").hasRole("ADMIN")
                            .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}

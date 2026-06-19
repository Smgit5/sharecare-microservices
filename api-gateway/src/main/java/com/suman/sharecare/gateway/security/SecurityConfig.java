package com.suman.sharecare.gateway.security;

import com.suman.sharecare.gateway.enums.UserRole;
import com.suman.sharecare.gateway.exception.JwtAccessDeniedHandler;
import com.suman.sharecare.gateway.exception.JwtAuthenticationEntryPoint;
import com.suman.sharecare.gateway.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        ex -> ex
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(
                    auth -> auth
                            .requestMatchers(HttpMethod.GET, "/donations/campaign/*/history").hasAnyRole(UserRole.NGO_REP.name(), UserRole.ADMIN.name())
                            .requestMatchers(HttpMethod.GET, "/donations/my").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.POST, "/donations").hasRole(UserRole.CITIZEN.name())
                            .requestMatchers(HttpMethod.GET, "/campaigns/my").hasRole(UserRole.NGO_REP.name())
                            .requestMatchers(HttpMethod.POST, "/campaigns").hasRole(UserRole.NGO_REP.name())
                            .requestMatchers(HttpMethod.PUT, "/campaigns/**").hasRole(UserRole.NGO_REP.name())
                            .requestMatchers(HttpMethod.PATCH, "/campaigns/*/approve", "/campaigns/*/reject", "/campaigns/*/send-back").hasRole(UserRole.ADMIN.name())
                            .requestMatchers(HttpMethod.PATCH, "/campaigns/*/close").hasAnyRole(UserRole.NGO_REP.name(), UserRole.ADMIN.name())
                            .requestMatchers("/auth/register", "/auth/login").permitAll()
                            .requestMatchers(HttpMethod.GET, "/campaigns/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/campaigns/filter/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/donations/test").permitAll()
                            .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}

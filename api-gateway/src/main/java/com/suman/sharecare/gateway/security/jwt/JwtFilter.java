package com.suman.sharecare.gateway.security.jwt;

import com.suman.sharecare.gateway.exception.JwtAuthenticationEntryPoint;
import com.suman.sharecare.gateway.security.CustomHeaderRequestWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CustomHeaderRequestWrapper requestWrapper = new CustomHeaderRequestWrapper(request);
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(!token.isBlank()) {
                try {
                    Claims claims = jwtService.extractAllClaims(token);
                    if(jwtService.isTokenValid(claims)) {
                        String username = jwtService.extractSubject(claims);
                        String role = jwtService.extractRole(claims);
                        String userId = jwtService.extractUserId(claims);
                        requestWrapper.addHeader("X-User-Id", userId);
                        requestWrapper.addHeader("X-Username", username);
                        requestWrapper.addHeader("X-User-Role", role);
                        if(SecurityContextHolder.getContext().getAuthentication() == null) {
                            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + role);
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(simpleGrantedAuthority)
                            );
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                } catch (JwtException ex) {
                    jwtAuthenticationEntryPoint.commence(request, response, new BadCredentialsException(ex.getMessage(), ex.getCause()));
                    return;
                }

            }
        }
        filterChain.doFilter(requestWrapper, response);
    }
}

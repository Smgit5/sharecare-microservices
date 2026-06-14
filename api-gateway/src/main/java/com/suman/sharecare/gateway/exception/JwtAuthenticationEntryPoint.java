package com.suman.sharecare.gateway.exception;

import com.suman.sharecare.gateway.dto.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Inside JwtAuthenticationEntryPoint :: commence, error = {}", authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ApiResponseDto apiResponseDto = new ApiResponseDto(response.getStatus(), "Unauthorized! Please login and try again.");
        String json = objectMapper.writeValueAsString(apiResponseDto);
        response.getWriter().write(json);

    }
}

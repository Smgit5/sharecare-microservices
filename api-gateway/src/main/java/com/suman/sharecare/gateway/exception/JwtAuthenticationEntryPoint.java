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
        Object authError = request.getAttribute("authError");
        String message;
        if("TOKEN_EXPIRED".equals(authError)) {
            message = "Your session has expired. Please log in again.";
        } else {
            message = "Unauthorized! Please login and try again.";
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto(response.getStatus(), message);
        String json = objectMapper.writeValueAsString(apiResponseDto);
        response.getWriter().write(json);
    }
}

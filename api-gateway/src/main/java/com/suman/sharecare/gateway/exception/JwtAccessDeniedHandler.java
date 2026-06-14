package com.suman.sharecare.gateway.exception;

import com.suman.sharecare.gateway.dto.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Inside JwtAccessDeniedHandler :: handle, error = {}", accessDeniedException.getMessage());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        ApiResponseDto apiResponseDto = new ApiResponseDto(HttpStatus.FORBIDDEN.value(), "You don't have permission to access this page.");
        String json = objectMapper.writeValueAsString(apiResponseDto);
        response.getWriter().write(json);
    }
}

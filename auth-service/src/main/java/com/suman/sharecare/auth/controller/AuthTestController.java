package com.suman.sharecare.auth.controller;

import com.suman.sharecare.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/test")
@RequiredArgsConstructor
public class AuthTestController {
    private final EmailService emailService;

    @GetMapping
    public String test() {
        return "Hello from AUTH-SERVICE through Gateway";
    }
}

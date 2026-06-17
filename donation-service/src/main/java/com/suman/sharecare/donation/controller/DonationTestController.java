package com.suman.sharecare.donation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donation")
public class DonationTestController {
    @GetMapping("/test")
    public ResponseEntity<String> testDonation() {
        return ResponseEntity.ok("DONATION-SERVICE via gateway.");
    }
}

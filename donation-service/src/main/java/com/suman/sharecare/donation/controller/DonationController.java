package com.suman.sharecare.donation.controller;

import com.suman.sharecare.donation.dto.DonationRequestDto;
import com.suman.sharecare.donation.dto.DonationResponseDto;
import com.suman.sharecare.donation.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<DonationResponseDto> donate(@RequestBody DonationRequestDto donationRequestDto, @RequestHeader("X-User-Id") String donorId) {
        return ResponseEntity.ok(donationService.donate(donationRequestDto, donorId));
    }
}

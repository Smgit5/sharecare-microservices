package com.suman.sharecare.donation.controller;

import com.suman.sharecare.donation.dto.donation_dtos.DonationRequestDto;
import com.suman.sharecare.donation.dto.donation_dtos.DonationResponseDto;
import com.suman.sharecare.donation.dto.donation_dtos.DonationStatisticsResponseDto;
import com.suman.sharecare.donation.dto.page_dtos.PageResponseDto;
import com.suman.sharecare.donation.service.DonationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/my")
    public ResponseEntity<PageResponseDto<DonationResponseDto>> getDonationHistoryOfCitizen(
            @RequestHeader("X-User-Id") String donorId,
            @ParameterObject
            @PageableDefault(sort = "donatedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(donationService.getDonationHistoryOfCitizen(donorId, pageable));
    }

    @GetMapping("/campaign/{campaignId}/history")
    public ResponseEntity<PageResponseDto<DonationResponseDto>> getDonationHistoryOfCampaign(
            @PathVariable String campaignId,
            @RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Role") String userRole,
            @ParameterObject
            @PageableDefault(sort = "donatedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(donationService.getDonationHistoryOfCampaign(campaignId, userId, userRole, pageable));
    }

    @GetMapping("/campaign/{campaignId}/stats")
    public ResponseEntity<DonationStatisticsResponseDto> getDonationStatistics(@PathVariable String campaignId, @RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Role") String userRole) {
        return ResponseEntity.ok(donationService.getDonationStatistics(campaignId, userId, userRole));
    }
}

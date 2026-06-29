package com.suman.sharecare.donation.controller;

import com.razorpay.RazorpayException;
import com.suman.sharecare.donation.dto.campaign_donation_dtos.CampaignAmountUpdateRequestDto;
import com.suman.sharecare.donation.dto.donation_dtos.*;
import com.suman.sharecare.donation.dto.page_dtos.ApiResponseDto;
import com.suman.sharecare.donation.dto.page_dtos.PageResponseDto;
import com.suman.sharecare.donation.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<DonationInitiationResponseDto> donate(@RequestBody DonationRequestDto donationRequestDto, @RequestHeader("X-User-Id") String donorId) throws RazorpayException {
        return ResponseEntity.ok(donationService.donate(donationRequestDto, donorId));
    }

    @PatchMapping("/payment-status/{paymentReferenceId}")
    public ResponseEntity<ApiResponseDto> checkPaymentStatus(@PathVariable String paymentReferenceId, @Valid @RequestBody PaymentStatusUpdateRequestDto paymentStatusUpdateRequestDto) {
        return ResponseEntity.ok(donationService.checkPaymentStatus(paymentReferenceId, paymentStatusUpdateRequestDto));
    }

    @PostMapping("/razorpay/verify")
    public ResponseEntity<ApiResponseDto> verifyRazorpayPayment(@Valid @RequestBody RazorpayPaymentVerificationRequestDto paymentVerificationRequestDto) throws RazorpayException {
        donationService.verifyRazorpayPayment(paymentVerificationRequestDto);
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK.value(), "Donation successful"));
    }

    @GetMapping("/{donationId}")
    public ResponseEntity<DonationResponseDto> getDonation(@PathVariable String donationId, @RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Roles") String userRoles) {
        return ResponseEntity.ok(donationService.getDonation(donationId, userId, userRoles));
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponseDto<DonationResponseDto>> viewMyDonationHistory(
            @RequestHeader("X-User-Id") String donorId,
            @ParameterObject
            @PageableDefault(sort = "paymentInitiatedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(donationService.viewMyDonationHistory(donorId, pageable));
    }

    @GetMapping("/campaign/{campaignId}/history")
    public ResponseEntity<PageResponseDto<DonationResponseDto>> getDonationHistoryOfCampaign(
            @PathVariable String campaignId,
            @RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Roles") String userRoles,
            @ParameterObject
            @PageableDefault(sort = "paymentInitiatedAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(donationService.getDonationHistoryOfCampaign(campaignId, userId, userRoles, pageable));
    }

    @GetMapping("/campaign/{campaignId}/stats")
    public ResponseEntity<DonationStatisticsResponseDto> getDonationStatistics(@PathVariable String campaignId, @RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Roles") String userRoles) {
        return ResponseEntity.ok(donationService.getDonationStatistics(campaignId, userId, userRoles));
    }

    @GetMapping("/my/campaign/{campaignId}/exists")
    public ResponseEntity<Boolean> isAlreadyDonated(@PathVariable String campaignId, @RequestHeader("X-User-Id") String citizenId) {
        return ResponseEntity.ok(donationService.isAlreadyDonated(campaignId, citizenId));
    }

    @GetMapping("/my/campaign/{campaignId}")
    public ResponseEntity<PageResponseDto<DonationResponseDto>> viewMyDonationsToCampaign(@PathVariable String campaignId, @RequestHeader("X-User-Id") String citizenId, @ParameterObject @PageableDefault(sort = "donatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(donationService.viewMyDonationsToCampaign(campaignId, citizenId, pageable));
    }
}

package com.suman.sharecare.campaign.controller;

import com.suman.sharecare.campaign.dto.PageDtos.ApiResponseDto;
import com.suman.sharecare.campaign.dto.PageDtos.PageResponseDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignAmountUpdateRequestDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignDonationCheckResponseDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignRequestDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignResponseDto;
import com.suman.sharecare.campaign.service.CampaignService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    // NGO_REP APIs - start
    @PostMapping
    public ResponseEntity<CampaignResponseDto> createCampaign(@RequestHeader("X-User-Id") String userId, @Valid @RequestBody CampaignRequestDto campaignRequestDto) {
        return ResponseEntity.ok(campaignService.createCampaign(userId, campaignRequestDto));
    }

    @PutMapping("/{campaignId}")
    public ResponseEntity<CampaignResponseDto> updateCampaign(@PathVariable UUID campaignId, @RequestHeader("X-User-Id") String userId, @Valid @RequestBody CampaignRequestDto campaignRequestDto) {
        return ResponseEntity.ok(campaignService.updateCampaign(campaignId, userId, campaignRequestDto));
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponseDto<CampaignResponseDto>> getMyCampaigns(
            @RequestHeader("X-User-Id") String userId,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(campaignService.getMyCampaigns(userId, pageable));
    }
    // NGO_REP APIs - end

    // ADMIN APIs - start
    @PatchMapping("/{campaignId}/approve")
    public ResponseEntity<CampaignResponseDto> approveCampaign(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignService.approveCampaign(campaignId));
    }

    @PatchMapping("/{campaignId}/send-back")
    public ResponseEntity<CampaignResponseDto> sendBackCampaign(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignService.sendBackCampaign(campaignId));
    }

    @PatchMapping("/{campaignId}/reject")
    public ResponseEntity<CampaignResponseDto> rejectCampaign(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignService.rejectCampaign(campaignId));
    }
    // ADMIN APIs - end

    // Both NGO_REP (Ownership required) and ADMIN apis - start
    @PatchMapping("/{campaignId}/close")
    public ResponseEntity<CampaignResponseDto> closeCampaign(@PathVariable UUID campaignId, @RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Role") String role) {
        return ResponseEntity.ok(campaignService.closeCampaign(campaignId, userId, role));
    }
    // Both NGO_REP (Ownership required) and ADMIN apis - end

    // Public APIs - start
    @GetMapping
    public ResponseEntity<PageResponseDto<CampaignResponseDto>> getCampaigns(
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(campaignService.getCampaigns(pageable));
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignResponseDto> getCampaignById(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignService.getCampaignById(campaignId));
    }

    @GetMapping("/filter")
    public ResponseEntity<PageResponseDto<CampaignResponseDto>> filterCampaigns(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ResponseEntity.ok(campaignService.filterCampaign(category, status, pageable));
    }
    // Public APIs - end

    // APIs needed for Donation-Service - start
    @GetMapping("/{campaignId}/donation-check")
    public ResponseEntity<CampaignDonationCheckResponseDto> checkIfDonationAllowed(@PathVariable String campaignId) {
        return ResponseEntity.ok(campaignService.checkIfDonationAllowed(campaignId));
    }

    @PatchMapping("/{campaignId}/raised-amount")
    public ResponseEntity<Void> updateRaisedAmount(@PathVariable String campaignId, @Valid @RequestBody CampaignAmountUpdateRequestDto amountUpdateRequestDto) {
        campaignService.updateRaisedAmount(campaignId, amountUpdateRequestDto);
        return ResponseEntity.ok().build();
    }
    // APIs needed for Donation-Service - end
}

package com.suman.sharecare.campaign.controller;

import com.suman.sharecare.campaign.dto.PageDtos.ApiResponseDto;
import com.suman.sharecare.campaign.dto.PageDtos.PageResponseDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignRequestDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignResponseDto;
import com.suman.sharecare.campaign.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

    @PostMapping("/ngo-rep")
    public ResponseEntity<CampaignResponseDto> createCampaign(@Valid @RequestBody CampaignRequestDto campaignRequestDto) {
        return ResponseEntity.ok(campaignService.createCampaign(campaignRequestDto));
    }

    @PutMapping("/{campaignId}/ngo-rep")
    public ResponseEntity<CampaignResponseDto> updateCampaign(@PathVariable UUID campaignId, @Valid @RequestBody CampaignRequestDto campaignRequestDto) {
        return ResponseEntity.ok(campaignService.updateCampaign(campaignId, campaignRequestDto));
    }

    @DeleteMapping("/{campaignId}/ngo-rep")
    public ResponseEntity<ApiResponseDto> deleteCampaign(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignService.deleteCampaign(campaignId));
    }

    @PatchMapping("/{campaignId}/admin/approve")
    public ResponseEntity<CampaignResponseDto> approveCampaign(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignService.approveCampaign(campaignId));
    }

    @PatchMapping("/{campaignId}/admin/reject")
    public ResponseEntity<CampaignResponseDto> rejectCampaign(@PathVariable UUID campaignId) {
        return ResponseEntity.ok(campaignService.rejectCampaign(campaignId));
    }

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
}

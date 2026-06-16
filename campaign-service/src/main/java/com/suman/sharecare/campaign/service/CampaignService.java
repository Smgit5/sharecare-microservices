package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.PageDtos.ApiResponseDto;
import com.suman.sharecare.campaign.dto.PageDtos.PageResponseDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignRequestDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignResponseDto;
import com.suman.sharecare.campaign.entity.Campaign;
import com.suman.sharecare.campaign.entity.CampaignCategory;
import com.suman.sharecare.campaign.entity.CampaignStatus;
import com.suman.sharecare.campaign.entity.Location;
import com.suman.sharecare.campaign.enums.UserRole;
import com.suman.sharecare.campaign.exception.ActionNotAllowedException;
import com.suman.sharecare.campaign.exception.custom_exception.ResourceNotFoundException;
import com.suman.sharecare.campaign.repository.CampaignRepository;
import com.suman.sharecare.campaign.utility.CampaignMapper;
import com.suman.sharecare.campaign.utility.PageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final StatusService statusService;
    private final CategoryService categoryService;
    private final LocationService locationService;

    public CampaignResponseDto createCampaign(String userId, CampaignRequestDto campaignRequestDto) {
        Campaign campaign = campaignMapper.toEntity(campaignRequestDto);
        campaign.setCreatedByUserId(UUID.fromString(userId));
        campaign.setStatus(statusService.getStatusByName(StatusService.PENDING_APPROVAL));
        campaign.setCategory(categoryService.getCategoryById(campaignRequestDto.getCategoryId()));
        campaign.setLocation(locationService.getLocationById(campaignRequestDto.getLocationId()));
        return campaignMapper.toDto(campaignRepository.save(campaign));
    }

    public PageResponseDto<CampaignResponseDto> getCampaigns(Pageable pageable) {
        Page<Campaign> pageOfCampaign = campaignRepository.findAll(pageable);
        Page<CampaignResponseDto> pageOfCampaignResponseDto = pageOfCampaign.map(campaignMapper::toDto);
        return PageMapper.toDto(pageOfCampaignResponseDto);
    }

    public CampaignResponseDto getCampaignById(UUID campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found!"));
        return campaignMapper.toDto(campaign);
    }

    public CampaignResponseDto updateCampaign(UUID campaignId, String userId, CampaignRequestDto campaignRequestDto) {
        Campaign existingCampaign = campaignRepository.findByIdAndCreatedByUserId(campaignId, UUID.fromString(userId)).orElseThrow(() -> new ResourceNotFoundException("Campaign Not found"));
        if(!existingCampaign.getStatus().getName().equals(StatusService.SENT_BACK)) {
            throw new ActionNotAllowedException("Only sent-back campaigns can be edited.");
        }
        campaignMapper.dtoToEntity(campaignRequestDto, existingCampaign);
        existingCampaign.setStatus(statusService.getStatusByName(StatusService.PENDING_APPROVAL));
        CampaignCategory updatedCategory = categoryService.getCategoryById(campaignRequestDto.getCategoryId());
        Location updatedLocation = locationService.getLocationById(campaignRequestDto.getLocationId());
        existingCampaign.setCategory(updatedCategory);
        existingCampaign.setLocation(updatedLocation);
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }

    public CampaignResponseDto sendBackCampaign(UUID campaignId) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found!"));
        if(!existingCampaign.getStatus().getName().equals(StatusService.PENDING_APPROVAL)) {
            throw new ActionNotAllowedException("This action can be performed only on campaigns pending for approval.");
        }
        existingCampaign.setStatus(statusService.getStatusByName(StatusService.SENT_BACK));
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }

    public CampaignResponseDto approveCampaign(UUID campaignId) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
        if(!existingCampaign.getStatus().getName().equals(StatusService.PENDING_APPROVAL)) {
            throw new ActionNotAllowedException("This action can be performed only on campaigns pending for approval.");
        }
        existingCampaign.setStatus(statusService.getStatusByName(StatusService.ACTIVE));
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }

    public CampaignResponseDto rejectCampaign(UUID campaignId) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
        if(!existingCampaign.getStatus().getName().equals(StatusService.PENDING_APPROVAL)) {
            throw new ActionNotAllowedException("This action can be performed only on campaigns pending for approval.");
        }
        existingCampaign.setStatus(statusService.getStatusByName(StatusService.REJECTED));
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }

    public PageResponseDto<CampaignResponseDto> getMyCampaigns(String userId, Pageable pageable) {
        Page<Campaign> campaigns = campaignRepository.findByCreatedByUserId(UUID.fromString(userId), pageable);
        Page<CampaignResponseDto> campaignResponseDtos = campaigns.map(campaignMapper::toDto);
        return PageMapper.toDto(campaignResponseDtos);
    }

    public CampaignResponseDto closeCampaign(UUID campaignId, String userId, String role) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found!"));
        if(!existingCampaign.getStatus().getName().equals(StatusService.ACTIVE)) {
            throw new ActionNotAllowedException("This action can be performed only on active campaigns.");
        }
        if(UserRole.NGO_REP.name().equals(role) && !userId.equals(existingCampaign.getCreatedByUserId().toString())) {
            throw new ResourceNotFoundException("Campaign not found!"); // for protecting ownership
        }
        existingCampaign.setStatus(statusService.getStatusByName(StatusService.CLOSED));
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }
}

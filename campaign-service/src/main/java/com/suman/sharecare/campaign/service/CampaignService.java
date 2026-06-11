package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.PageDtos.ApiResponseDto;
import com.suman.sharecare.campaign.dto.PageDtos.PageResponseDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignRequestDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignResponseDto;
import com.suman.sharecare.campaign.entity.Campaign;
import com.suman.sharecare.campaign.entity.CampaignCategory;
import com.suman.sharecare.campaign.entity.CampaignStatus;
import com.suman.sharecare.campaign.entity.Location;
import com.suman.sharecare.campaign.exception.custom_exception.ResourceNotFoundException;
import com.suman.sharecare.campaign.repository.CampaignRepository;
import com.suman.sharecare.campaign.utility.CampaignMapper;
import com.suman.sharecare.campaign.utility.PageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private static final UUID TEST_USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final StatusService statusService;
    private final CategoryService categoryService;
    private final LocationService locationService;

    public CampaignResponseDto createCampaign(CampaignRequestDto campaignRequestDto) {
        Campaign campaign = campaignMapper.toEntity(campaignRequestDto);
        campaign.setCreatedByUserId(TEST_USER_ID);
        campaign.setStatus(statusService.getDefaultStatus());
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

    public CampaignResponseDto updateCampaign(UUID campaignId, @Valid CampaignRequestDto campaignRequestDto) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign Not found"));
        campaignMapper.dtoToEntity(campaignRequestDto, existingCampaign);
        CampaignCategory updatedCategory = categoryService.getCategoryById(campaignRequestDto.getCategoryId());
        Location updatedLocation = locationService.getLocationById(campaignRequestDto.getLocationId());
        existingCampaign.setCategory(updatedCategory);
        existingCampaign.setLocation(updatedLocation);
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }

    public CampaignResponseDto approveCampaign(UUID campaignId) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
        existingCampaign.setStatus(statusService.getStatusById(StatusService.ACTIVE_STATUS_ID));
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }

    public CampaignResponseDto rejectCampaign(UUID campaignId) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
        existingCampaign.setStatus(statusService.getStatusById(StatusService.REJECT_STATUS_ID));
        return campaignMapper.toDto(campaignRepository.save(existingCampaign));
    }

    public ApiResponseDto deleteCampaign(UUID campaignId) {
        Campaign existingCampaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
        campaignRepository.delete(existingCampaign);
        return new ApiResponseDto(HttpStatus.OK.value(), "Selected campaign has been deleted.");
    }
}

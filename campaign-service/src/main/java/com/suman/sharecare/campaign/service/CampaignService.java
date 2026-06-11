package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.PageDtos.PageResponseDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignRequestDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignResponseDto;
import com.suman.sharecare.campaign.entity.Campaign;
import com.suman.sharecare.campaign.exception.custom_exception.ResourceNotFoundException;
import com.suman.sharecare.campaign.repository.CampaignRepository;
import com.suman.sharecare.campaign.utility.CampaignMapper;
import com.suman.sharecare.campaign.utility.PageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}

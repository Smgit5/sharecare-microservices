package com.suman.sharecare.campaign.utility;

import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignRequestDto;
import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignResponseDto;
import com.suman.sharecare.campaign.entity.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = LocationMapper.class)
public interface CampaignMapper {

    @Mapping(source = "status.name", target = "status")
    @Mapping(source = "category.name", target = "category")
    CampaignResponseDto toDto(Campaign campaign);

    Campaign toEntity(CampaignRequestDto campaignRequestDto);

    void dtoToEntity(CampaignRequestDto campaignRequestDto, @MappingTarget Campaign campaign);
}

package com.suman.sharecare.campaign.utility;

import com.suman.sharecare.campaign.dto.status_dtos.StatusResponseDto;
import com.suman.sharecare.campaign.entity.CampaignStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusResponseDto toDto(CampaignStatus status);
}

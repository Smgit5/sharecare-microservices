package com.suman.sharecare.campaign.utility;

import com.suman.sharecare.campaign.dto.category_dtos.CategoryResponseDto;
import com.suman.sharecare.campaign.entity.CampaignCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(CampaignCategory category);
}

package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.category_dtos.CategoryResponseDto;
import com.suman.sharecare.campaign.entity.CampaignCategory;
import com.suman.sharecare.campaign.repository.CampaignCategoryRepository;
import com.suman.sharecare.campaign.utility.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CampaignCategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDto> getCategories() {
        List<CampaignCategory> campaignCategories = categoryRepository.findAll();
        return campaignCategories.stream().map(categoryMapper::toDto).toList();
    }
}

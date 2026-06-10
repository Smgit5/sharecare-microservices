package com.suman.sharecare.campaign.service;

import com.suman.sharecare.campaign.dto.category_dtos.CategoryResponseDto;
import com.suman.sharecare.campaign.entity.CampaignCategory;
import com.suman.sharecare.campaign.repository.CategoryRepository;
import com.suman.sharecare.campaign.utility.CategoryMapper;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponseDto> getCategories() {
        List<CampaignCategory> campaignCategories = categoryRepository.findAll();
        return campaignCategories.stream().map(categoryMapper::toDto).toList();
    }

    public CampaignCategory getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }
}

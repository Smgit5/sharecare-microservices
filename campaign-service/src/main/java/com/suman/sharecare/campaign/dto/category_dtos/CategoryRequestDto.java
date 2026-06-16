package com.suman.sharecare.campaign.dto.category_dtos;

import com.suman.sharecare.campaign.enums.Categories;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDto {
    private Categories category;
}

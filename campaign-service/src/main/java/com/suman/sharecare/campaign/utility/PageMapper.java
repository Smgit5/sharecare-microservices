package com.suman.sharecare.campaign.utility;

import com.suman.sharecare.campaign.dto.PageDtos.PageResponseDto;
import org.springframework.data.domain.Page;

public class PageMapper {
    public static <T> PageResponseDto<T> toDto(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}

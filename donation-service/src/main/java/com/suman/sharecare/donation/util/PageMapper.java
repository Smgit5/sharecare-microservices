package com.suman.sharecare.donation.util;

import com.suman.sharecare.donation.dto.page_dtos.PageResponseDto;
import org.springframework.data.domain.Page;

public class PageMapper {
    public static <T> PageResponseDto<T> generateResponseDto(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }
}

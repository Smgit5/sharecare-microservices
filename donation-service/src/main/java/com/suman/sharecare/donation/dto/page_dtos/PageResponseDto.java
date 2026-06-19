package com.suman.sharecare.donation.dto.page_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> content;
    private int page;
    private int size;
    private int numberOfElements;
    private int totalPages;
    private long totalElements;
    private boolean lastPage;
}

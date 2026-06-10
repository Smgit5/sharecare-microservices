package com.suman.sharecare.campaign.dto.PageDtos;

import com.suman.sharecare.campaign.dto.campaign_dtos.CampaignResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {
    private List<T> content;
    private int page;
    private int size;
    private int numberOfElements;
    private long totalElemets;
    private int totalPages;
    private boolean lastPage;
}

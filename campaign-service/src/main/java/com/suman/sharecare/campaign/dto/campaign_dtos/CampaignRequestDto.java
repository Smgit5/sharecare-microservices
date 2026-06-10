package com.suman.sharecare.campaign.dto.campaign_dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CampaignRequestDto {
    private String title;
    private String description;
    private BigDecimal targetAmount;
    private UUID categoryId;
    private UUID locationId;
}

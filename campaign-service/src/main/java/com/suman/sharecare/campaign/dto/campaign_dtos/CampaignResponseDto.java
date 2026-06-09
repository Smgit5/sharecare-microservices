package com.suman.sharecare.campaign.dto.campaign_dtos;

import com.suman.sharecare.campaign.dto.location_dtos.LocationResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CampaignResponseDto {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal raisedAmount;
    private UUID createdByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private String category;
    private LocationResponseDto location;
}

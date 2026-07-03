package com.suman.sharecare.campaign.dto.campaign_dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.suman.sharecare.campaign.dto.location_dtos.LocationResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private String status;
    private String category;
    private LocationResponseDto location;

    public BigDecimal getProgressPercentage() {
        return raisedAmount.multiply(BigDecimal.valueOf(100)).divide(targetAmount, 2, RoundingMode.HALF_UP);
    }
}

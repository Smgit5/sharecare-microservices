package com.suman.sharecare.campaign.dto.campaign_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CampaignRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 150)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 10000)
    private String description;

    @NotNull(message = "Target Amount is required")
    @Positive(message = "Target Amount must be greater than 0")
    private BigDecimal targetAmount;

    @NotNull(message = "Category is required")
    private UUID categoryId;

    @NotNull(message = "Location is required")
    private UUID locationId;

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}

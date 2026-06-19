package com.suman.sharecare.donation.dto.campaign_donation_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CampaignAmountUpdateRequestDto {
    private BigDecimal amount;
}

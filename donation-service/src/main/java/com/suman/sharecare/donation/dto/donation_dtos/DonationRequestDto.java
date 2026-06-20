package com.suman.sharecare.donation.dto.donation_dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DonationRequestDto {
    private String campaignId;
    private BigDecimal amount;
}

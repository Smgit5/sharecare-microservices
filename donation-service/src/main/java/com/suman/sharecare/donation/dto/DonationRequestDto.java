package com.suman.sharecare.donation.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class DonationRequestDto {
    private UUID campaignId;
    private BigDecimal amount;
}

package com.suman.sharecare.donation.dto.donation_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DonationStatisticsResponseDto {
    private long totalDonations;
    private BigDecimal totalAmount;
}

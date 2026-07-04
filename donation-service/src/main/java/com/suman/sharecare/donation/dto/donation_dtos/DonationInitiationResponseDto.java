package com.suman.sharecare.donation.dto.donation_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class DonationInitiationResponseDto {
    private String keyId;
    private Integer amountInPaise;
    private String currency;
    private String providerOrderId;
}

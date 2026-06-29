package com.suman.sharecare.donation.dto.donation_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@AllArgsConstructor
public class DonationInitiationResponseDto {
    private String providerOrderId;
    private String keyId;
}

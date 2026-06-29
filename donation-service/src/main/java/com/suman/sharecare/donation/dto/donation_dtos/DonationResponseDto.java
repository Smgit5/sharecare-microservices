package com.suman.sharecare.donation.dto.donation_dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DonationResponseDto {
    private UUID id;
    private UUID campaignId;
    private UUID donorId;
    private BigDecimal amount;
    private String status;
    private String paymentReferenceId;
    private String providerOrderId;
    private String providerPaymentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentInitiatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paidAt;
}
package com.suman.sharecare.donation.dto.donation_dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentStatusUpdateRequestDto {
    @NotBlank(message = "Payment status is required.")
    private String paymentStatus;
}

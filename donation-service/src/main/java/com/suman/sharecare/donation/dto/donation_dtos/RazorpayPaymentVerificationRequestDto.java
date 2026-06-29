package com.suman.sharecare.donation.dto.donation_dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RazorpayPaymentVerificationRequestDto {
    @NotBlank(message = "Order id is required.")
    private String razorpayOrderId;

    @NotBlank(message = "Payment id is required.")
    private String razorpayPaymentId;

    @NotBlank(message = "Signature is required.")
    private String razorpaySignature;
}

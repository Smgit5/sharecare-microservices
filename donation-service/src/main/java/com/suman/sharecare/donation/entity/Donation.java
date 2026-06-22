package com.suman.sharecare.donation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID campaignId;

    @Column(nullable = false)
    private UUID donorId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private DonationStatus status;

    @Column(nullable = false)
    private LocalDateTime paymentInitiatedAt;

    private LocalDateTime paidAt;

    @Column(unique = true)
    private String paymentReferenceId;

    @PrePersist
    public void onCreate() {
        this.paymentInitiatedAt = LocalDateTime.now();
    }
}

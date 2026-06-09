package com.suman.sharecare.campaign.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal raisedAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private UUID createdByUserId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private CampaignStatus status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CampaignCategory category;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

package com.suman.sharecare.campaign.repository;

import com.suman.sharecare.campaign.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    Optional<Campaign> findByIdAndCreatedByUserId(UUID campaignId, UUID userId);

    Page<Campaign> findByCreatedByUserId(UUID userId, Pageable pageable);
}

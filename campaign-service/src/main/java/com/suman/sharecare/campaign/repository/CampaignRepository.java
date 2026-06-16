package com.suman.sharecare.campaign.repository;

import com.suman.sharecare.campaign.entity.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    Optional<Campaign> findByIdAndCreatedByUserId(UUID campaignId, UUID userId);

    Page<Campaign> findByCreatedByUserId(UUID userId, Pageable pageable);
    @Query(
            """
               SELECT c FROM Campaign c
               WHERE (:category IS NULL OR c.category.name = :category)
               AND (:status IS NULL OR c.status.name = :status)
            """
    )
    Page<Campaign> findByFilters(String category, String status, Pageable pageable);
}

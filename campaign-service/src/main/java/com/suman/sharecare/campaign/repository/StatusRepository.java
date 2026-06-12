package com.suman.sharecare.campaign.repository;

import com.suman.sharecare.campaign.entity.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusRepository extends JpaRepository<CampaignStatus, UUID> {
    Optional<CampaignStatus> findByName(String status);
}

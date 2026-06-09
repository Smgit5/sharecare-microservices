package com.suman.sharecare.campaign.repository;

import com.suman.sharecare.campaign.entity.CampaignCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CampaignCategory, UUID> {
}

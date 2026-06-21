package com.suman.sharecare.donation.repository;

import com.suman.sharecare.donation.entity.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonationRespository extends JpaRepository<Donation, UUID> {
    Page<Donation> findAllByDonorId(UUID donorId, Pageable pageable);
    Page<Donation> findByCampaignId(UUID campaignId, Pageable pageable);
    long countByCampaignId(UUID campaignId);

    @Query("""
                            SELECT COALESCE(SUM(amount), 0)
                            FROM Donation d
                            WHERE d.campaignId=:campaignId
""")
    BigDecimal sumAmountByCampaignId(UUID campaignId);

    Boolean existsByCampaignIdAndDonorId(UUID campaignId, UUID donorId);
    Page<Donation> findAllByCampaignIdAndDonorId(UUID campaignId, UUID donorId, Pageable pageable);
    Optional<Donation> findByPaymentReferenceId(String paymentReferenceId);
}

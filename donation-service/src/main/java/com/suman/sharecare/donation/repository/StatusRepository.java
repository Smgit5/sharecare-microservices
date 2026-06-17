package com.suman.sharecare.donation.repository;

import com.suman.sharecare.donation.entity.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusRepository extends JpaRepository<DonationStatus, UUID> {
    boolean existsByStatus(String status);
    Optional<DonationStatus> findByStatus(String status);
}
